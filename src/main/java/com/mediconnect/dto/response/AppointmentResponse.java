package com.mediconnect.dto.response;

import com.mediconnect.entity.AppointmentStatus;
import com.mediconnect.entity.ConsultationType;
import com.mediconnect.entity.Specialization;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {

    private Long id;
    private String appointmentReference;

    private Long patientId;
    private String patientName;
    private String patientEmail;

    private Long doctorId;
    private String doctorName;
    private String doctorEmail;
    private Specialization specialization;
    private String clinicName;
    private String clinicAddress;

    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private ConsultationType consultationType;
    private AppointmentStatus status;
    private Double fee;
    private String notes;
    private String cancellationReason;
    private String rescheduleReason;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}