package com.mediconnect.controller;

import com.mediconnect.dto.request.DelegationRequest;
import com.mediconnect.dto.response.DelegationResponse;
import com.mediconnect.service.DelegationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/delegations")
@RequiredArgsConstructor
public class DelegationController {

    private final DelegationService delegationService;

    @PostMapping("/doctor/{doctorId}")
    public ResponseEntity<DelegationResponse> createDelegation(
            @PathVariable Long doctorId,
            @Valid @RequestBody DelegationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(delegationService.createDelegation(doctorId, request));
    }

    @GetMapping("/doctor/{doctorId}/active")
    public ResponseEntity<List<DelegationResponse>> getActiveDelegations(
            @PathVariable Long doctorId) {
        return ResponseEntity.ok(delegationService.getActiveDelegations(doctorId));
    }

    @PatchMapping("/{delegationId}/revoke")
    public ResponseEntity<DelegationResponse> revokeDelegation(
            @PathVariable Long delegationId) {
        return ResponseEntity.ok(delegationService.revokeDelegation(delegationId));
    }
}