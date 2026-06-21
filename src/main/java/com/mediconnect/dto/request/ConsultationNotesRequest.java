package com.mediconnect.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ConsultationNotesRequest {

    @NotBlank(message = "Please provide a valid symptoms")
    private String symptoms;

    @NotBlank(message = "Please provide a valid diagnosis")
    private String diagnosis;

    @NotBlank(message = "Please provide a valid treatmentPlan")
    private String treatmentPlan;

    private String followUpDate;

    private String doctorNotes;
}