package com.mediconnect.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class RescheduleRequest {

    @NotNull(message = "Please provide a valid appointmentDate")
    @Future(message = "Please provide a valid appointmentDate")
    private LocalDate appointmentDate;

    @NotNull(message = "Please provide a valid appointmentTime")
    private LocalTime appointmentTime;

    @NotBlank(message = "Please provide a valid rescheduleReason")
    private String rescheduleReason;
}