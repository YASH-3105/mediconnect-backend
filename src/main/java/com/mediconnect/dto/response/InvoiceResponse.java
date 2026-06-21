package com.mediconnect.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResponse {
    private String invoiceNumber;
    private String transactionId;
    private String patientName;
    private String patientEmail;
    private String doctorName;
    private String specialization;
    private String appointmentReference;
    private String appointmentDate;
    private String consultationType;
    private Double consultationFee;
    private Double platformFee;
    private Double tax;
    private Double totalAmount;
    private String currency;
    private String paymentMethod;
    private String paymentStatus;
    private LocalDateTime paidAt;
}