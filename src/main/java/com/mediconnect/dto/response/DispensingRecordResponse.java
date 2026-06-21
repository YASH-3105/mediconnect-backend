package com.mediconnect.dto.response;

import com.mediconnect.entity.DispenseStatus;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DispensingRecordResponse {
    private Long id;
    private Long prescriptionId;
    private String prescriptionNumber;
    private Long pharmacyUserId;
    private String pharmacyName;
    private Long patientId;
    private String patientName;
    private DispenseStatus status;
    private String notes;
    private String rejectionReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}