package com.mediconnect.controller;

import com.mediconnect.dto.request.PaymentRequest;
import com.mediconnect.dto.request.RefundRequest;
import com.mediconnect.dto.response.InvoiceResponse;
import com.mediconnect.dto.response.PagedResponse;
import com.mediconnect.dto.response.PaymentResponse;
import com.mediconnect.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/patient/{patientId}/initiate")
    public ResponseEntity<PaymentResponse> initiatePayment(
            @PathVariable Long patientId,
            @Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(paymentService.initiatePayment(patientId, request));
    }

    @PatchMapping("/confirm/{transactionId}")
    public ResponseEntity<PaymentResponse> confirmPayment(
            @PathVariable String transactionId) {
        return ResponseEntity.ok(paymentService.confirmPayment(transactionId));
    }

    @PostMapping("/{paymentId}/refund")
    public ResponseEntity<PaymentResponse> refundPayment(
            @PathVariable Long paymentId,
            @Valid @RequestBody RefundRequest request) {
        return ResponseEntity.ok(paymentService.refundPayment(paymentId, request));
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<PaymentResponse> getByAppointmentId(
            @PathVariable Long appointmentId) {
        return ResponseEntity.ok(paymentService.getPaymentByAppointmentId(appointmentId));
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getById(
            @PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.getPaymentById(paymentId));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<PagedResponse<PaymentResponse>> getPatientPayments(
            @PathVariable Long patientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
                paymentService.getPatientPayments(patientId, page, size));
    }

    @GetMapping("/{paymentId}/invoice")
    public ResponseEntity<InvoiceResponse> getInvoice(
            @PathVariable Long paymentId) {
        return ResponseEntity.ok(paymentService.getInvoice(paymentId));
    }
}