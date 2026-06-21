package com.mediconnect.controller;

import com.mediconnect.dto.response.AnalyticsResponse;
import com.mediconnect.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/platform")
    public ResponseEntity<AnalyticsResponse> getPlatformAnalytics() {
        return ResponseEntity.ok(analyticsService.getPlatformAnalytics());
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<AnalyticsResponse> getDoctorAnalytics(
            @PathVariable Long doctorId) {
        return ResponseEntity.ok(analyticsService.getDoctorAnalytics(doctorId));
    }
}