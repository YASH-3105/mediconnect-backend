package com.mediconnect.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.List;

@Data
public class PrescriptionRequest {

    @NotNull(message = "Please provide a valid consultationId")
    private Long consultationId;

    @NotNull(message = "Please provide a valid patientId")
    private Long patientId;

    @NotEmpty(message = "Please provide at least one medicine")
    private List<PrescriptionItemRequest> items;

    private boolean repeatPrescription;

    private String validUntil;

    private String additionalInstructions;
}