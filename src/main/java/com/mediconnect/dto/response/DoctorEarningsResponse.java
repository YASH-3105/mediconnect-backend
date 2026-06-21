package com.mediconnect.dto.response;

import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorEarningsResponse {
    private Long doctorId;
    private String doctorName;
    private String specialization;
    private double grossEarnings;
    private double platformCommission;
    private double netEarnings;
    private long totalConsultations;
    private long paidConsultations;
    private List<PaymentResponse> paymentHistory;
}