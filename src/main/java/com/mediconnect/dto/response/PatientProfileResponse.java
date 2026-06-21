package com.mediconnect.dto.response;

import com.mediconnect.entity.Gender;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientProfileResponse {

    private Long id;
    private Long userId;
    private String fullName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String bloodGroup;
    private Double weightKg;
    private Double heightCm;
    private String chronicConditions;
    private String allergies;
    private String pastSurgeries;
    private String familyHistory;
    private String emergencyContactName;
    private String emergencyContactRelation;
    private String emergencyContactPhone;
    private String profilePhotoUrl;
    private int profileCompletionPercent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}