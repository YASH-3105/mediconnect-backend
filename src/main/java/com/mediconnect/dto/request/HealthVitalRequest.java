package com.mediconnect.dto.request;

import com.mediconnect.entity.VitalType;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HealthVitalRequest {

    @NotNull(message = "Please provide a valid vitalType")
    private VitalType vitalType;

    @NotNull(message = "Please provide a valid value")
    @Positive(message = "Please provide a valid value")
    private Double value;

    @NotBlank(message = "Please provide a valid unit")
    private String unit;

    private String notes;

    @NotNull(message = "Please provide a valid recordedAt")
    private LocalDateTime recordedAt;
}