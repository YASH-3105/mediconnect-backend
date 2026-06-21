package com.mediconnect.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardResponse {
    private long totalUsers;
    private long totalPatients;
    private long totalDoctors;
    private long totalPharmacies;
    private long totalAppointments;
    private long totalAppointmentsToday;
    private long pendingDoctorApprovals;
    private long totalPayments;
    private double totalRevenue;
}