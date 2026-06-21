package com.mediconnect.service;

import com.mediconnect.dto.request.DoctorProfileRequest;
import com.mediconnect.dto.response.DoctorProfileResponse;
import com.mediconnect.dto.response.PagedResponse;
import com.mediconnect.entity.Specialization;

public interface DoctorProfileService {
    DoctorProfileResponse createOrUpdateProfile(Long userId, DoctorProfileRequest request);
    DoctorProfileResponse getProfileByUserId(Long userId);
    DoctorProfileResponse getProfileById(Long doctorId);
    DoctorProfileResponse approveDoctor(Long doctorId);
    PagedResponse<DoctorProfileResponse> searchDoctors(
            Specialization specialization,
            String city,
            Double minFee,
            Double maxFee,
            Boolean acceptingNewPatients,
            int page, int size, String sortBy);
    PagedResponse<DoctorProfileResponse> getAllApprovedDoctors(int page, int size);
}