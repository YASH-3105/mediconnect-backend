package com.mediconnect.dto.response;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeaveBlockResponse {
    private Long id;
    private Long doctorId;
    private String doctorName;
    private LocalDate leaveDate;
    private String reason;
    private LocalDateTime createdAt;
}