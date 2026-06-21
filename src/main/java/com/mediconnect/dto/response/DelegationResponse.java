package com.mediconnect.dto.response;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DelegationResponse {
    private Long id;
    private Long doctorId;
    private String doctorName;
    private Long coordinatorId;
    private String coordinatorName;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean active;
    private String reason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}