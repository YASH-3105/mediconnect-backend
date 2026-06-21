package com.mediconnect.dto.request;

import com.mediconnect.entity.Specialization;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class DoctorProfileRequest {

    @NotBlank(message = "Please provide a valid licenseNumber")
    private String licenseNumber;

    @NotNull(message = "Please provide a valid specialization")
    private Specialization specialization;

    @NotBlank(message = "Please provide a valid qualification")
    private String qualification;

    private String clinicName;

    private String clinicAddress;

    @NotBlank(message = "Please provide a valid city")
    private String city;

    private String languagesSpoken;

    @Min(value = 0, message = "Please provide a valid experienceYears")
    private Integer experienceYears;

    @Positive(message = "Please provide a valid consultationFee")
    private Double consultationFee;

    private String bio;
}