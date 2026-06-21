package com.mediconnect.dto.response;

import com.mediconnect.entity.PaymentMethod;
import com.mediconnect.entity.PaymentStatus;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private Long id;
    private String transactionId;
    private Long appointmentId;
    private String appointmentReference;
    private Long patientId;
    private String patientName;
    private String doctorName;
    private Double amount;
    private Double refundAmount;
    private String currency;
    private PaymentMethod paymentMethod;
    private PaymentStatus status;
    private String gatewayOrderId;
    private String gatewayPaymentId;
    private String failureReason;
    private String refundReason;
    private LocalDateTime paidAt;
    private LocalDateTime refundedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}