package com.mediconnect.dto.response;

import com.mediconnect.entity.Day;
import lombok.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorScheduleResponse {
    private Long id;
    private Long doctorId;
    private String doctorName;
    private Day dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer slotDurationMinutes;
    private Integer bufferTimeMinutes;
    private Integer dailyPatientCap;
    private boolean active;
    private List<String> availableSlots;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}