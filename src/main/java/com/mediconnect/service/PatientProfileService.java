package com.mediconnect.service;

import com.mediconnect.dto.request.PatientProfileRequest;
import com.mediconnect.dto.response.PatientProfileResponse;

public interface PatientProfileService {
    PatientProfileResponse createOrUpdateProfile(Long userId, PatientProfileRequest request);
    PatientProfileResponse getProfileByUserId(Long userId);
}