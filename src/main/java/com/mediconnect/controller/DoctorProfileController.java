package com.mediconnect.controller;

import com.mediconnect.dto.request.DoctorProfileRequest;
import com.mediconnect.dto.response.DoctorProfileResponse;
import com.mediconnect.dto.response.PagedResponse;
import com.mediconnect.entity.Specialization;
import com.mediconnect.service.DoctorProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
public class DoctorProfileController {

    private final DoctorProfileService doctorProfileService;

    @PostMapping("/{userId}/profile")
    public ResponseEntity<DoctorProfileResponse> createOrUpdateProfile(
            @PathVariable Long userId,
            @Valid @RequestBody DoctorProfileRequest request) {
        return ResponseEntity.ok(doctorProfileService.createOrUpdateProfile(userId, request));
    }

    @GetMapping("/{userId}/profile")
    public ResponseEntity<DoctorProfileResponse> getProfileByUserId(
            @PathVariable Long userId) {
        return ResponseEntity.ok(doctorProfileService.getProfileByUserId(userId));
    }

    @GetMapping("/detail/{doctorId}")
    public ResponseEntity<DoctorProfileResponse> getProfileById(
            @PathVariable Long doctorId) {
        return ResponseEntity.ok(doctorProfileService.getProfileById(doctorId));
    }

    @GetMapping("/search")
    public ResponseEntity<PagedResponse<DoctorProfileResponse>> searchDoctors(
            @RequestParam(required = false) Specialization specialization,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Double minFee,
            @RequestParam(required = false) Double maxFee,
            @RequestParam(required = false) Boolean acceptingNewPatients,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "averageRating") String sortBy) {
        return ResponseEntity.ok(doctorProfileService.searchDoctors(
                specialization, city, minFee, maxFee, acceptingNewPatients, page, size, sortBy));
    }

    @GetMapping
    public ResponseEntity<PagedResponse<DoctorProfileResponse>> getAllDoctors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(doctorProfileService.getAllApprovedDoctors(page, size));
    }
    
    @PatchMapping("/{doctorId}/approve")
    public ResponseEntity<DoctorProfileResponse> approveDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(doctorProfileService.approveDoctor(doctorId));
    }
}