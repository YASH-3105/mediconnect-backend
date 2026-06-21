package com.mediconnect.dto.request;

import com.mediconnect.entity.Day;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalTime;

@Data
public class DoctorScheduleRequest {

    @NotNull(message = "Please provide a valid dayOfWeek")
    private Day dayOfWeek;

    @NotNull(message = "Please provide a valid startTime")
    private LocalTime startTime;

    @NotNull(message = "Please provide a valid endTime")
    private LocalTime endTime;

    @NotNull(message = "Please provide a valid slotDurationMinutes")
    @Min(value = 10, message = "Slot duration must be at least 10 minutes")
    private Integer slotDurationMinutes;

    @Min(value = 0, message = "Please provide a valid bufferTimeMinutes")
    private Integer bufferTimeMinutes;

    @Positive(message = "Please provide a valid dailyPatientCap")
    private Integer dailyPatientCap;
}