package com.mediconnect.dto.response;

import com.mediconnect.entity.ConsultationStatus;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationResponse {

    private Long id;
    private Long appointmentId;
    private String appointmentReference;

    private Long patientId;
    private String patientName;

    private Long doctorId;
    private String doctorName;
    private String specialization;

    private ConsultationStatus status;
    private String symptoms;
    private String diagnosis;
    private String treatmentPlan;
    private String followUpDate;
    private String doctorNotes;

    private String videoSessionId;
    private String videoJoinUrl;
    private LocalDateTime sessionStartTime;
    private LocalDateTime sessionEndTime;
    private Long durationMinutes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}