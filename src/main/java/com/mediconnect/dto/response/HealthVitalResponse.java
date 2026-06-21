package com.mediconnect.dto.response;

import com.mediconnect.entity.VitalType;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthVitalResponse {
    private Long id;
    private Long patientId;
    private String patientName;
    private VitalType vitalType;
    private Double value;
    private String unit;
    private String notes;
    private boolean abnormal;
    private String normalRange;
    private LocalDateTime recordedAt;
    private LocalDateTime createdAt;
}