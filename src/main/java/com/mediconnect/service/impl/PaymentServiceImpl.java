package com.mediconnect.service.impl;

import com.mediconnect.dto.request.PaymentRequest;
import com.mediconnect.dto.request.RefundRequest;
import com.mediconnect.dto.response.InvoiceResponse;
import com.mediconnect.dto.response.PagedResponse;
import com.mediconnect.dto.response.PaymentResponse;
import com.mediconnect.entity.*;
import com.mediconnect.exception.BadRequestException;
import com.mediconnect.exception.ResourceNotFoundException;
import com.mediconnect.repository.*;
import com.mediconnect.service.NotificationService;
import com.mediconnect.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    private static final double PLATFORM_FEE_PERCENT = 5.0;
    private static final double TAX_PERCENT = 18.0;

    @Override
    public PaymentResponse initiatePayment(Long patientId, PaymentRequest request) {
        log.info("Initiating payment for patientId: {}", patientId);

        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Patient not found with id: " + patientId));

        Appointment appointment = appointmentRepository.findById(request.getAppointmentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appointment not found with id: " + request.getAppointmentId()));

        if (paymentRepository.findByAppointmentId(request.getAppointmentId()).isPresent()) {
            throw new BadRequestException("Payment already exists for this appointment.");
        }

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new BadRequestException("Cannot initiate payment for a cancelled appointment.");
        }

        double consultationFee = appointment.getFee() != null ? appointment.getFee() : 0.0;
        double platformFee = consultationFee * PLATFORM_FEE_PERCENT / 100;
        double tax = (consultationFee + platformFee) * TAX_PERCENT / 100;
        double totalAmount = consultationFee + platformFee + tax;

        // Simulate gateway order ID
        String gatewayOrderId = "ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Payment payment = Payment.builder()
                .appointment(appointment)
                .patient(patient)
                .amount(Math.round(totalAmount * 100.0) / 100.0)
                .currency(request.getCurrency())
                .paymentMethod(request.getPaymentMethod())
                .status(PaymentStatus.PENDING)
                .gatewayOrderId(gatewayOrderId)
                .build();

        Payment saved = paymentRepository.save(payment);
        log.info("Payment initiated with transactionId: {}", saved.getTransactionId());

        return mapToResponse(saved);
    }

    @Override
    public PaymentResponse confirmPayment(String transactionId) {
        log.info("Confirming payment: {}", transactionId);

        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Payment not found with transactionId: " + transactionId));

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            throw new BadRequestException("Payment already confirmed.");
        }

        // Simulate gateway payment ID
        String gatewayPaymentId = "PAY-" +
                UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setGatewayPaymentId(gatewayPaymentId);
        payment.setPaidAt(LocalDateTime.now());

        Payment saved = paymentRepository.save(payment);

        // Send notification
        notificationService.sendNotification(
                payment.getPatient(),
                "Payment Successful",
                "Your payment of " + payment.getCurrency() + " " +
                payment.getAmount() + " for appointment " +
                payment.getAppointment().getAppointmentReference() +
                " was successful.",
                NotificationType.PAYMENT_SUCCESS,
                "/payments/" + saved.getId()
        );

        log.info("Payment confirmed: {}", transactionId);
        return mapToResponse(saved);
    }

    @Override
    public PaymentResponse refundPayment(Long paymentId, RefundRequest request) {
        log.info("Processing refund for paymentId: {}", paymentId);

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Payment not found with id: " + paymentId));

        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new BadRequestException("Only successful payments can be refunded.");
        }

        double refundAmount = request.getRefundAmount() != null
                ? request.getRefundAmount()
                : payment.getAmount();

        if (refundAmount > payment.getAmount()) {
            throw new BadRequestException("Refund amount cannot exceed payment amount.");
        }

        payment.setRefundAmount(refundAmount);
        payment.setRefundReason(request.getRefundReason());
        payment.setRefundedAt(LocalDateTime.now());
        payment.setStatus(Double.compare(refundAmount, payment.getAmount()) == 0
                ? PaymentStatus.REFUNDED
                : PaymentStatus.PARTIALLY_REFUNDED);

        Payment saved = paymentRepository.save(payment);

        notificationService.sendNotification(
                payment.getPatient(),
                "Refund Processed",
                "Your refund of " + payment.getCurrency() + " " +
                refundAmount + " has been processed.",
                NotificationType.PAYMENT_FAILED,
                "/payments/" + saved.getId()
        );

        log.info("Refund processed for paymentId: {}", paymentId);
        return mapToResponse(saved);
    }

    @Override
    public PaymentResponse getPaymentByAppointmentId(Long appointmentId) {
        Payment payment = paymentRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Payment not found for appointmentId: " + appointmentId));
        return mapToResponse(payment);
    }

    @Override
    public PaymentResponse getPaymentById(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Payment not found with id: " + paymentId));
        return mapToResponse(payment);
    }

    @Override
    public PagedResponse<PaymentResponse> getPatientPayments(
            Long patientId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Payment> result = paymentRepository.findByPatientId(patientId, pageable);
        return buildPagedResponse(result);
    }

    @Override
    public InvoiceResponse getInvoice(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Payment not found with id: " + paymentId));

        Appointment appointment = payment.getAppointment();
        double consultationFee = appointment.getFee() != null ? appointment.getFee() : 0.0;
        double platformFee = Math.round(consultationFee * PLATFORM_FEE_PERCENT) / 100.0;
        double tax = Math.round((consultationFee + platformFee) * TAX_PERCENT) / 100.0;

        return InvoiceResponse.builder()
                .invoiceNumber("INV-" + payment.getTransactionId())
                .transactionId(payment.getTransactionId())
                .patientName(payment.getPatient().getFullName())
                .patientEmail(payment.getPatient().getEmail())
                .doctorName(appointment.getDoctor().getUser().getFullName())
                .specialization(appointment.getDoctor().getSpecialization().name())
                .appointmentReference(appointment.getAppointmentReference())
                .appointmentDate(appointment.getAppointmentDate().toString())
                .consultationType(appointment.getConsultationType().name())
                .consultationFee(consultationFee)
                .platformFee(platformFee)
                .tax(tax)
                .totalAmount(payment.getAmount())
                .currency(payment.getCurrency())
                .paymentMethod(payment.getPaymentMethod().name())
                .paymentStatus(payment.getStatus().name())
                .paidAt(payment.getPaidAt())
                .build();
    }

    private PagedResponse<PaymentResponse> buildPagedResponse(Page<Payment> page) {
        List<PaymentResponse> content = page.getContent()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return PagedResponse.<PaymentResponse>builder()
                .content(content)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    private PaymentResponse mapToResponse(Payment p) {
        return PaymentResponse.builder()
                .id(p.getId())
                .transactionId(p.getTransactionId())
                .appointmentId(p.getAppointment().getId())
                .appointmentReference(p.getAppointment().getAppointmentReference())
                .patientId(p.getPatient().getId())
                .patientName(p.getPatient().getFullName())
                .doctorName(p.getAppointment().getDoctor().getUser().getFullName())
                .amount(p.getAmount())
                .refundAmount(p.getRefundAmount())
                .currency(p.getCurrency())
                .paymentMethod(p.getPaymentMethod())
                .status(p.getStatus())
                .gatewayOrderId(p.getGatewayOrderId())
                .gatewayPaymentId(p.getGatewayPaymentId())
                .failureReason(p.getFailureReason())
                .refundReason(p.getRefundReason())
                .paidAt(p.getPaidAt())
                .refundedAt(p.getRefundedAt())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .build();
    }
}