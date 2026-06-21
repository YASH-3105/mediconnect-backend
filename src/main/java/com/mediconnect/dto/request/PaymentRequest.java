package com.mediconnect.dto.request;

import com.mediconnect.entity.PaymentMethod;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PaymentRequest {

    @NotNull(message = "Please provide a valid appointmentId")
    private Long appointmentId;

    @NotNull(message = "Please provide a valid paymentMethod")
    private PaymentMethod paymentMethod;

    private String currency = "INR";
}