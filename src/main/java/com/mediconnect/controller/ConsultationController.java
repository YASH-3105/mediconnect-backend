package com.mediconnect.controller;

import com.mediconnect.dto.request.ConsultationNotesRequest;
import com.mediconnect.dto.response.ConsultationResponse;
import com.mediconnect.dto.response.PagedResponse;
import com.mediconnect.service.ConsultationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/consultations")
@RequiredArgsConstructor
public class ConsultationController {

    private final ConsultationService consultationService;

    @PostMapping("/appointment/{appointmentId}")
    public ResponseEntity<ConsultationResponse> createConsultation(
            @PathVariable Long appointmentId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(consultationService.createConsultation(appointmentId));
    }

    @PatchMapping("/{consultationId}/start")
    public ResponseEntity<ConsultationResponse> startSession(
            @PathVariable Long consultationId) {
        return ResponseEntity.ok(consultationService.startVideoSession(consultationId));
    }

    @PatchMapping("/{consultationId}/end")
    public ResponseEntity<ConsultationResponse> endSession(
            @PathVariable Long consultationId) {
        return ResponseEntity.ok(consultationService.endVideoSession(consultationId));
    }

    @PutMapping("/{consultationId}/notes")
    public ResponseEntity<ConsultationResponse> saveNotes(
            @PathVariable Long consultationId,
            @Valid @RequestBody ConsultationNotesRequest request) {
        return ResponseEntity.ok(consultationService.saveNotes(consultationId, request));
    }

    @PatchMapping("/{consultationId}/complete")
    public ResponseEntity<ConsultationResponse> completeConsultation(
            @PathVariable Long consultationId) {
        return ResponseEntity.ok(consultationService.completeConsultation(consultationId));
    }

    @GetMapping("/{consultationId}")
    public ResponseEntity<ConsultationResponse> getById(
            @PathVariable Long consultationId) {
        return ResponseEntity.ok(consultationService.getConsultationById(consultationId));
    }

    @GetMapping("/appointment/{appointmentId}")
    public ResponseEntity<ConsultationResponse> getByAppointmentId(
            @PathVariable Long appointmentId) {
        return ResponseEntity.ok(
                consultationService.getConsultationByAppointmentId(appointmentId));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<PagedResponse<ConsultationResponse>> getPatientConsultations(
            @PathVariable Long patientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
                consultationService.getPatientConsultations(patientId, page, size));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<PagedResponse<ConsultationResponse>> getDoctorConsultations(
            @PathVariable Long doctorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
                consultationService.getDoctorConsultations(doctorId, page, size));
    }
}