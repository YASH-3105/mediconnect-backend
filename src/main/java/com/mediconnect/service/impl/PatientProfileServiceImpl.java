package com.mediconnect.service.impl;

import com.mediconnect.dto.request.PatientProfileRequest;
import com.mediconnect.dto.response.PatientProfileResponse;
import com.mediconnect.entity.PatientProfile;
import com.mediconnect.entity.User;
import com.mediconnect.exception.BadRequestException;
import com.mediconnect.exception.ResourceNotFoundException;
import com.mediconnect.repository.PatientProfileRepository;
import com.mediconnect.repository.UserRepository;
import com.mediconnect.service.PatientProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientProfileServiceImpl implements PatientProfileService {

    private final PatientProfileRepository patientProfileRepository;
    private final UserRepository userRepository;

    @Override
    public PatientProfileResponse createOrUpdateProfile(Long userId, PatientProfileRequest request) {
        log.info("Creating/updating patient profile for userId: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (!user.getRole().name().equals("PATIENT")) {
            throw new BadRequestException("Only patients can have a patient profile.");
        }

        PatientProfile profile = patientProfileRepository.findByUserId(userId)
                .orElse(PatientProfile.builder().user(user).build());

        profile.setDateOfBirth(request.getDateOfBirth());
        profile.setGender(request.getGender());
        profile.setBloodGroup(request.getBloodGroup());
        profile.setWeightKg(request.getWeightKg());
        profile.setHeightCm(request.getHeightCm());
        profile.setChronicConditions(request.getChronicConditions());
        profile.setAllergies(request.getAllergies());
        profile.setPastSurgeries(request.getPastSurgeries());
        profile.setFamilyHistory(request.getFamilyHistory());
        profile.setEmergencyContactName(request.getEmergencyContactName());
        profile.setEmergencyContactRelation(request.getEmergencyContactRelation());
        profile.setEmergencyContactPhone(request.getEmergencyContactPhone());

        PatientProfile saved = patientProfileRepository.save(profile);
        log.info("Patient profile saved for userId: {}", userId);

        return mapToResponse(saved);
    }

    @Override
    public PatientProfileResponse getProfileByUserId(Long userId) {
        log.info("Fetching patient profile for userId: {}", userId);

        PatientProfile profile = patientProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found for userId: " + userId));

        return mapToResponse(profile);
    }

    private PatientProfileResponse mapToResponse(PatientProfile profile) {
        return PatientProfileResponse.builder()
                .id(profile.getId())
                .userId(profile.getUser().getId())
                .fullName(profile.getUser().getFullName())
                .email(profile.getUser().getEmail())
                .phone(profile.getUser().getPhone())
                .dateOfBirth(profile.getDateOfBirth())
                .gender(profile.getGender())
                .bloodGroup(profile.getBloodGroup())
                .weightKg(profile.getWeightKg())
                .heightCm(profile.getHeightCm())
                .chronicConditions(profile.getChronicConditions())
                .allergies(profile.getAllergies())
                .pastSurgeries(profile.getPastSurgeries())
                .familyHistory(profile.getFamilyHistory())
                .emergencyContactName(profile.getEmergencyContactName())
                .emergencyContactRelation(profile.getEmergencyContactRelation())
                .emergencyContactPhone(profile.getEmergencyContactPhone())
                .profilePhotoUrl(profile.getProfilePhotoUrl())
                .profileCompletionPercent(calculateCompletion(profile))
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }

    private int calculateCompletion(PatientProfile p) {
        int total = 10;
        int filled = 0;
        if (p.getDateOfBirth() != null) filled++;
        if (p.getGender() != null) filled++;
        if (p.getBloodGroup() != null) filled++;
        if (p.getWeightKg() != null) filled++;
        if (p.getHeightCm() != null) filled++;
        if (p.getChronicConditions() != null) filled++;
        if (p.getAllergies() != null) filled++;
        if (p.getEmergencyContactName() != null) filled++;
        if (p.getEmergencyContactPhone() != null) filled++;
        if (p.getProfilePhotoUrl() != null) filled++;
        return (filled * 100) / total;
    }
}