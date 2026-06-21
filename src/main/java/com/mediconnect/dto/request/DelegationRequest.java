package com.mediconnect.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class DelegationRequest {

    @NotNull(message = "Please provide a valid coordinatorId")
    private Long coordinatorId;

    @NotNull(message = "Please provide a valid startDate")
    @FutureOrPresent(message = "Please provide a valid startDate")
    private LocalDate startDate;

    @NotNull(message = "Please provide a valid endDate")
    @Future(message = "Please provide a valid endDate")
    private LocalDate endDate;

    private String reason;
}