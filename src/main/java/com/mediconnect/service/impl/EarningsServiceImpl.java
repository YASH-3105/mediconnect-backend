package com.mediconnect.service.impl;

import com.mediconnect.dto.response.DoctorEarningsResponse;
import com.mediconnect.dto.response.PaymentResponse;
import com.mediconnect.entity.*;
import com.mediconnect.exception.ResourceNotFoundException;
import com.mediconnect.repository.*;
import com.mediconnect.service.EarningsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EarningsServiceImpl implements EarningsService {

    private final PaymentRepository paymentRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final AppointmentRepository appointmentRepository;

    private static final double PLATFORM_COMMISSION = 5.0;

    @Override
    public DoctorEarningsResponse getDoctorEarnings(Long doctorId) {
        log.info("Fetching earnings for doctorId: {}", doctorId);

        DoctorProfile doctor = doctorProfileRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Doctor not found with id: " + doctorId));

        List<Payment> doctorPayments = paymentRepository.findAll()
                .stream()
                .filter(p -> p.getAppointment().getDoctor().getId().equals(doctorId)
                        && p.getStatus() == PaymentStatus.SUCCESS)
                .collect(Collectors.toList());

        double grossEarnings = doctorPayments.stream()
                .mapToDouble(p -> p.getAmount() != null ? p.getAmount() : 0.0)
                .sum();

        double platformCommission = grossEarnings * PLATFORM_COMMISSION / 100;
        double netEarnings = grossEarnings - platformCommission;

        long totalConsultations = appointmentRepository.findAll()
                .stream()
                .filter(a -> a.getDoctor().getId().equals(doctorId))
                .count();

        List<PaymentResponse> paymentHistory = doctorPayments.stream()
                .map(p -> PaymentResponse.builder()
                        .id(p.getId())
                        .transactionId(p.getTransactionId())
                        .appointmentId(p.getAppointment().getId())
                        .appointmentReference(p.getAppointment().getAppointmentReference())
                        .patientName(p.getPatient().getFullName())
                        .amount(p.getAmount())
                        .currency(p.getCurrency())
                        .paymentMethod(p.getPaymentMethod())
                        .status(p.getStatus())
                        .paidAt(p.getPaidAt())
                        .createdAt(p.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return DoctorEarningsResponse.builder()
                .doctorId(doctorId)
                .doctorName(doctor.getUser().getFullName())
                .specialization(doctor.getSpecialization().name())
                .grossEarnings(Math.round(grossEarnings * 100.0) / 100.0)
                .platformCommission(Math.round(platformCommission * 100.0) / 100.0)
                .netEarnings(Math.round(netEarnings * 100.0) / 100.0)
                .totalConsultations(totalConsultations)
                .paidConsultations(doctorPayments.size())
                .paymentHistory(paymentHistory)
                .build();
    }
}