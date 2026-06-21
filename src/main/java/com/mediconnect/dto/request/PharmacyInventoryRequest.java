package com.mediconnect.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PharmacyInventoryRequest {

    @NotBlank(message = "Please provide a valid medicineName")
    private String medicineName;

    private String genericName;

    private String manufacturer;

    @NotNull(message = "Please provide a valid stockQuantity")
    @Min(value = 0, message = "Please provide a valid stockQuantity")
    private Integer stockQuantity;

    @NotNull(message = "Please provide a valid reorderThreshold")
    @Min(value = 1, message = "Please provide a valid reorderThreshold")
    private Integer reorderThreshold;

    @Positive(message = "Please provide a valid unitPrice")
    private Double unitPrice;
}