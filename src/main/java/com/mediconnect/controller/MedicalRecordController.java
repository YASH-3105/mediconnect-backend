package com.mediconnect.controller;

import com.mediconnect.dto.request.MedicalRecordRequest;
import com.mediconnect.dto.response.MedicalRecordResponse;
import com.mediconnect.dto.response.PagedResponse;
import com.mediconnect.entity.RecordCategory;
import com.mediconnect.service.MedicalRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.mediconnect.entity.RecordCategory;

@RestController
@RequestMapping("/api/v1/records")
@RequiredArgsConstructor
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @PostMapping(value = "/patient/{patientId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
public ResponseEntity<MedicalRecordResponse> uploadRecord(
       @PathVariable Long patientId,
       @RequestParam("file") MultipartFile file,
       @RequestParam("category") String category,
       @RequestParam(value = "doctorName", required = false) String doctorName,
       @RequestParam(value = "labName", required = false) String labName,
       @RequestParam("recordDate") String recordDate,
       @RequestParam(value = "notes", required = false) String notes) {

   MedicalRecordRequest request = new MedicalRecordRequest();
   request.setCategory(RecordCategory.valueOf(category));
   request.setDoctorName(doctorName);
   request.setLabName(labName);
   request.setRecordDate(java.time.LocalDate.parse(recordDate));
   request.setNotes(notes);

   return ResponseEntity.status(HttpStatus.CREATED)
           .body(medicalRecordService.uploadRecord(patientId, file, request));
}

    @GetMapping("/{recordId}")
    public ResponseEntity<MedicalRecordResponse> getById(
            @PathVariable Long recordId) {
        return ResponseEntity.ok(medicalRecordService.getRecordById(recordId));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<PagedResponse<MedicalRecordResponse>> getPatientRecords(
            @PathVariable Long patientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
                medicalRecordService.getPatientRecords(patientId, page, size));
    }

    @GetMapping("/patient/{patientId}/category/{category}")
    public ResponseEntity<PagedResponse<MedicalRecordResponse>> getByCategory(
            @PathVariable Long patientId,
            @PathVariable RecordCategory category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(medicalRecordService.getPatientRecordsByCategory(
                patientId, category, page, size));
    }

    @PostMapping("/{recordId}/share")
    public ResponseEntity<MedicalRecordResponse> generateShareToken(
            @PathVariable Long recordId) {
        return ResponseEntity.ok(medicalRecordService.generateShareToken(recordId));
    }

    @GetMapping("/shared/{token}")
    public ResponseEntity<MedicalRecordResponse> getByShareToken(
            @PathVariable String token) {
        return ResponseEntity.ok(medicalRecordService.getRecordByShareToken(token));
    }

    @DeleteMapping("/{recordId}/patient/{patientId}")
    public ResponseEntity<Void> deleteRecord(
            @PathVariable Long recordId,
            @PathVariable Long patientId) {
        medicalRecordService.deleteRecord(recordId, patientId);
        return ResponseEntity.noContent().build();
    }
}