package com.mediconnect.dto.request;

import com.mediconnect.entity.DispenseStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class DispenseRequest {

    @NotNull(message = "Please provide a valid status")
    private DispenseStatus status;

    private String notes;

    private String rejectionReason;
}