package com.mediconnect.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    private Long id;
    private Long patientId;
    private String patientName;
    private Long doctorId;
    private String doctorName;
    private Long appointmentId;
    private Integer rating;
    private String reviewText;
    private String doctorResponse;
    private boolean flagged;
    private boolean hidden;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}