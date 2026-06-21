package com.mediconnect.dto.request;

import com.mediconnect.entity.Gender;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class PatientProfileRequest {

    @NotNull(message = "Please provide a valid dateOfBirth")
    @Past(message = "Please provide a valid dateOfBirth")
    private LocalDate dateOfBirth;

    @NotNull(message = "Please provide a valid gender")
    private Gender gender;

    @NotBlank(message = "Please provide a valid bloodGroup")
    private String bloodGroup;

    @Positive(message = "Please provide a valid weightKg")
    private Double weightKg;

    @Positive(message = "Please provide a valid heightCm")
    private Double heightCm;

    private String chronicConditions;

    private String allergies;

    private String pastSurgeries;

    private String familyHistory;

    @NotBlank(message = "Please provide a valid emergencyContactName")
    private String emergencyContactName;

    @NotBlank(message = "Please provide a valid emergencyContactRelation")
    private String emergencyContactRelation;

    @NotBlank(message = "Please provide a valid emergencyContactPhone")
    @Pattern(regexp = "^[0-9]{10}$", message = "Please provide a valid emergencyContactPhone")
    private String emergencyContactPhone;
}