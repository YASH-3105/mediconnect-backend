package com.mediconnect.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PlatformConfigRequest {

    @NotBlank(message = "Please provide a valid configKey")
    private String configKey;

    @NotBlank(message = "Please provide a valid configValue")
    private String configValue;

    private String description;

    @NotBlank(message = "Please provide a valid category")
    private String category;
}