package com.mediconnect.controller;

import com.mediconnect.dto.request.PrescriptionRequest;
import com.mediconnect.dto.response.PagedResponse;
import com.mediconnect.dto.response.PrescriptionResponse;
import com.mediconnect.entity.Medicine;
import com.mediconnect.service.PrescriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @PostMapping("/doctor/{doctorId}")
    public ResponseEntity<PrescriptionResponse> createPrescription(
            @PathVariable Long doctorId,
            @Valid @RequestBody PrescriptionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(prescriptionService.createPrescription(doctorId, request));
    }

    @GetMapping("/{prescriptionId}")
    public ResponseEntity<PrescriptionResponse> getPrescriptionById(
            @PathVariable Long prescriptionId) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionById(prescriptionId));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<PagedResponse<PrescriptionResponse>> getPatientPrescriptions(
            @PathVariable Long patientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
                prescriptionService.getPatientPrescriptions(patientId, page, size));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<PagedResponse<PrescriptionResponse>> getDoctorPrescriptions(
            @PathVariable Long doctorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
                prescriptionService.getDoctorPrescriptions(doctorId, page, size));
    }

    @GetMapping("/consultation/{consultationId}")
    public ResponseEntity<List<PrescriptionResponse>> getConsultationPrescriptions(
            @PathVariable Long consultationId) {
        return ResponseEntity.ok(
                prescriptionService.getConsultationPrescriptions(consultationId));
    }

    @GetMapping("/medicines/search")
    public ResponseEntity<List<Medicine>> searchMedicines(
            @RequestParam String query) {
        return ResponseEntity.ok(prescriptionService.searchMedicines(query));
    }
}