package com.mediconnect.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CancelRequest {

    @NotBlank(message = "Please provide a valid cancellationReason")
    private String cancellationReason;
}