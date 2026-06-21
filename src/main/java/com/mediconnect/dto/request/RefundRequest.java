package com.mediconnect.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RefundRequest {

    @NotBlank(message = "Please provide a valid refundReason")
    private String refundReason;

    private Double refundAmount;
}