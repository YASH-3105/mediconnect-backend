package com.mediconnect.controller;

import com.mediconnect.dto.request.HealthVitalRequest;
import com.mediconnect.dto.response.HealthVitalResponse;
import com.mediconnect.dto.response.PagedResponse;
import com.mediconnect.entity.VitalType;
import com.mediconnect.service.HealthVitalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/vitals")
@RequiredArgsConstructor
public class HealthVitalController {

    private final HealthVitalService vitalService;

    @PostMapping("/patient/{patientId}")
    public ResponseEntity<HealthVitalResponse> logVital(
            @PathVariable Long patientId,
            @Valid @RequestBody HealthVitalRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(vitalService.logVital(patientId, request));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<PagedResponse<HealthVitalResponse>> getPatientVitals(
            @PathVariable Long patientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
                vitalService.getPatientVitals(patientId, page, size));
    }

    @GetMapping("/patient/{patientId}/trend")
    public ResponseEntity<List<HealthVitalResponse>> getVitalTrend(
            @PathVariable Long patientId,
            @RequestParam VitalType type,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime end) {
        return ResponseEntity.ok(
                vitalService.getVitalTrend(patientId, type, start, end));
    }

    @DeleteMapping("/{vitalId}/patient/{patientId}")
    public ResponseEntity<Void> deleteVital(
            @PathVariable Long vitalId,
            @PathVariable Long patientId) {
        vitalService.deleteVital(vitalId, patientId);
        return ResponseEntity.noContent().build();
    }
}