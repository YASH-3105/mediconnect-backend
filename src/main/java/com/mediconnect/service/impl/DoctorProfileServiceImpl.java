package com.mediconnect.service.impl;

import com.mediconnect.dto.request.DoctorProfileRequest;
import com.mediconnect.dto.response.DoctorProfileResponse;
import com.mediconnect.dto.response.PagedResponse;
import com.mediconnect.entity.DoctorProfile;
import com.mediconnect.entity.Specialization;
import com.mediconnect.entity.User;
import com.mediconnect.exception.BadRequestException;
import com.mediconnect.exception.ResourceNotFoundException;
import com.mediconnect.repository.DoctorProfileRepository;
import com.mediconnect.repository.UserRepository;
import com.mediconnect.service.DoctorProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorProfileServiceImpl implements DoctorProfileService {

    private final DoctorProfileRepository doctorProfileRepository;
    private final UserRepository userRepository;

    @Override
    public DoctorProfileResponse createOrUpdateProfile(Long userId, DoctorProfileRequest request) {
        log.info("Creating/updating doctor profile for userId: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        if (!user.getRole().name().equals("DOCTOR")) {
            throw new BadRequestException("Only doctors can have a doctor profile.");
        }

        if (doctorProfileRepository.existsByLicenseNumber(request.getLicenseNumber())) {
            DoctorProfile existing = doctorProfileRepository.findByUserId(userId).orElse(null);
            if (existing == null || !existing.getLicenseNumber().equals(request.getLicenseNumber())) {
                throw new BadRequestException("License number already registered.");
            }
        }

        DoctorProfile profile = doctorProfileRepository.findByUserId(userId)
                .orElse(DoctorProfile.builder().user(user).build());

        profile.setLicenseNumber(request.getLicenseNumber());
        profile.setSpecialization(request.getSpecialization());
        profile.setQualification(request.getQualification());
        profile.setClinicName(request.getClinicName());
        profile.setClinicAddress(request.getClinicAddress());
        profile.setCity(request.getCity());
        profile.setLanguagesSpoken(request.getLanguagesSpoken());
        profile.setExperienceYears(request.getExperienceYears());
        profile.setConsultationFee(request.getConsultationFee());
        profile.setBio(request.getBio());

        DoctorProfile saved = doctorProfileRepository.save(profile);
        log.info("Doctor profile saved for userId: {}", userId);

        return mapToResponse(saved);
    }

    @Override
    public DoctorProfileResponse getProfileByUserId(Long userId) {
        DoctorProfile profile = doctorProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor profile not found for userId: " + userId));
        return mapToResponse(profile);
    }

    @Override
    public DoctorProfileResponse getProfileById(Long doctorId) {
        DoctorProfile profile = doctorProfileRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor profile not found with id: " + doctorId));
        return mapToResponse(profile);
    }

    @Override
    public PagedResponse<DoctorProfileResponse> searchDoctors(
            Specialization specialization, String city,
            Double minFee, Double maxFee,
            Boolean acceptingNewPatients,
            int page, int size, String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        Page<DoctorProfile> result = doctorProfileRepository.searchDoctors(
                specialization, city, minFee, maxFee, acceptingNewPatients, pageable);

        return buildPagedResponse(result);
    }

    @Override
    public PagedResponse<DoctorProfileResponse> getAllApprovedDoctors(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("averageRating").descending());
        Page<DoctorProfile> result = doctorProfileRepository.findByApprovedTrue(pageable);
        return buildPagedResponse(result);
    }
    
    @Override
    public DoctorProfileResponse approveDoctor(Long doctorId) {
        log.info("Approving doctor with id: {}", doctorId);
        DoctorProfile profile = doctorProfileRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + doctorId));
        profile.setApproved(true);
        profile.setAcceptingNewPatients(true);
        DoctorProfile saved = doctorProfileRepository.save(profile);
        return mapToResponse(saved);
    }

    private PagedResponse<DoctorProfileResponse> buildPagedResponse(Page<DoctorProfile> page) {
        List<DoctorProfileResponse> content = page.getContent()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return PagedResponse.<DoctorProfileResponse>builder()
                .content(content)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    private DoctorProfileResponse mapToResponse(DoctorProfile profile) {
        return DoctorProfileResponse.builder()
                .id(profile.getId())
                .userId(profile.getUser().getId())
                .fullName(profile.getUser().getFullName())
                .email(profile.getUser().getEmail())
                .phone(profile.getUser().getPhone())
                .licenseNumber(profile.getLicenseNumber())
                .specialization(profile.getSpecialization())
                .qualification(profile.getQualification())
                .clinicName(profile.getClinicName())
                .clinicAddress(profile.getClinicAddress())
                .city(profile.getCity())
                .languagesSpoken(profile.getLanguagesSpoken())
                .experienceYears(profile.getExperienceYears())
                .consultationFee(profile.getConsultationFee())
                .averageRating(profile.getAverageRating())
                .totalRatings(profile.getTotalRatings())
                .acceptingNewPatients(profile.isAcceptingNewPatients())
                .approved(profile.isApproved())
                .profilePhotoUrl(profile.getProfilePhotoUrl())
                .bio(profile.getBio())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }
}