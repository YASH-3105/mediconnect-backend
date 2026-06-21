package com.mediconnect.dto.request;

import com.mediconnect.entity.ConsultationType;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AppointmentRequest {

    @NotNull(message = "Please provide a valid doctorId")
    private Long doctorId;

    @NotNull(message = "Please provide a valid appointmentDate")
    @Future(message = "Please provide a valid appointmentDate")
    private LocalDate appointmentDate;

    @NotNull(message = "Please provide a valid appointmentTime")
    private LocalTime appointmentTime;

    @NotNull(message = "Please provide a valid consultationType")
    private ConsultationType consultationType;

    private String notes;
}