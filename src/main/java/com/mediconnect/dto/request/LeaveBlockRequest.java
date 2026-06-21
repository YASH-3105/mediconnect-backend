package com.mediconnect.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class LeaveBlockRequest {

    @NotNull(message = "Please provide a valid leaveDate")
    @Future(message = "Please provide a valid leaveDate")
    private LocalDate leaveDate;

    private String reason;
}