package com.mediconnect.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PrescriptionItemRequest {

    @NotBlank(message = "Please provide a valid medicineName")
    private String medicineName;

    @NotBlank(message = "Please provide a valid dosage")
    private String dosage;

    @NotBlank(message = "Please provide a valid frequency")
    private String frequency;

    @NotBlank(message = "Please provide a valid duration")
    private String duration;

    private String instructions;
}