package com.mediconnect.dto.response;

import lombok.*;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsResponse {
    private long totalAppointments;
    private long completedAppointments;
    private long cancelledAppointments;
    private long pendingAppointments;
    private double totalRevenue;
    private double averageConsultationFee;
    private long totalPatients;
    private long totalDoctors;
    private long totalPrescriptions;
    private long totalMedicalRecords;
    private Map<String, Long> appointmentsByStatus;
    private Map<String, Long> appointmentsByConsultationType;
    private Map<String, Double> revenueBySpecialization;
}