package com.mediconnect.dto.response;

import com.mediconnect.entity.Specialization;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorProfileResponse {

    private Long id;
    private Long userId;
    private String fullName;
    private String email;
    private String phone;
    private String licenseNumber;
    private Specialization specialization;
    private String qualification;
    private String clinicName;
    private String clinicAddress;
    private String city;
    private String languagesSpoken;
    private Integer experienceYears;
    private Double consultationFee;
    private Double averageRating;
    private Integer totalRatings;
    private boolean acceptingNewPatients;
    private boolean approved;
    private String profilePhotoUrl;
    private String bio;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}