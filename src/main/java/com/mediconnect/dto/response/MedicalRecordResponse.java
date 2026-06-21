package com.mediconnect.dto.response;

import com.mediconnect.entity.RecordCategory;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordResponse {
    private Long id;
    private Long patientId;
    private String patientName;
    private String fileName;
    private String originalFileName;
    private String fileType;
    private Long fileSize;
    private RecordCategory category;
    private String doctorName;
    private String labName;
    private LocalDate recordDate;
    private String notes;
    private String shareToken;
    private LocalDateTime shareTokenExpiry;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}