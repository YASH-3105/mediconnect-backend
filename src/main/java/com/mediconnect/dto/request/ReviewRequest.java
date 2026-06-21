package com.mediconnect.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ReviewRequest {

    @NotNull(message = "Please provide a valid appointmentId")
    private Long appointmentId;

    @NotNull(message = "Please provide a valid rating")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    @Size(max = 500, message = "Review text cannot exceed 500 characters")
    private String reviewText;
}