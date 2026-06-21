package com.mediconnect.controller;

import com.mediconnect.dto.request.PatientProfileRequest;
import com.mediconnect.dto.response.PatientProfileResponse;
import com.mediconnect.service.PatientProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
public class PatientProfileController {

    private final PatientProfileService patientProfileService;

    @PostMapping("/{userId}/profile")
    public ResponseEntity<PatientProfileResponse> createOrUpdateProfile(
            @PathVariable Long userId,
            @Valid @RequestBody PatientProfileRequest request) {
        return ResponseEntity.ok(patientProfileService.createOrUpdateProfile(userId, request));
    }

    @GetMapping("/{userId}/profile")
    public ResponseEntity<PatientProfileResponse> getProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(patientProfileService.getProfileByUserId(userId));
    }
}