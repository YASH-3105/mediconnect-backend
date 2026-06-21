package com.mediconnect.service;

import com.mediconnect.dto.request.PaymentRequest;
import com.mediconnect.dto.request.RefundRequest;
import com.mediconnect.dto.response.InvoiceResponse;
import com.mediconnect.dto.response.PagedResponse;
import com.mediconnect.dto.response.PaymentResponse;

public interface PaymentService {
    PaymentResponse initiatePayment(Long patientId, PaymentRequest request);
    PaymentResponse confirmPayment(String transactionId);
    PaymentResponse refundPayment(Long paymentId, RefundRequest request);
    PaymentResponse getPaymentByAppointmentId(Long appointmentId);
    PaymentResponse getPaymentById(Long paymentId);
    PagedResponse<PaymentResponse> getPatientPayments(Long patientId, int page, int size);
    InvoiceResponse getInvoice(Long paymentId);
}