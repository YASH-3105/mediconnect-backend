package com.mediconnect.dto.response;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionResponse {
    private Long id;
    private String prescriptionNumber;
    private Long consultationId;
    private Long doctorId;
    private String doctorName;
    private String specialization;
    private Long patientId;
    private String patientName;
    private List<PrescriptionItemResponse> items;
    private boolean repeatPrescription;
    private String validUntil;
    private String additionalInstructions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}