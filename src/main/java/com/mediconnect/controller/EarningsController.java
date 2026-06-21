package com.mediconnect.controller;

import com.mediconnect.dto.response.DoctorEarningsResponse;
import com.mediconnect.service.EarningsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/earnings")
@RequiredArgsConstructor
public class EarningsController {

    private final EarningsService earningsService;

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<DoctorEarningsResponse> getDoctorEarnings(
            @PathVariable Long doctorId) {
        return ResponseEntity.ok(earningsService.getDoctorEarnings(doctorId));
    }
}