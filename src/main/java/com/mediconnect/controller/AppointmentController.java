package com.mediconnect.controller;

import com.mediconnect.dto.request.AppointmentRequest;
import com.mediconnect.dto.request.CancelRequest;
import com.mediconnect.dto.request.RescheduleRequest;
import com.mediconnect.dto.response.AppointmentResponse;
import com.mediconnect.dto.response.PagedResponse;
import com.mediconnect.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping("/patient/{patientId}")
    public ResponseEntity<AppointmentResponse> bookAppointment(
            @PathVariable Long patientId,
            @Valid @RequestBody AppointmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(appointmentService.bookAppointment(patientId, request));
    }

    @PutMapping("/{appointmentId}/reschedule/patient/{patientId}")
    public ResponseEntity<AppointmentResponse> rescheduleAppointment(
            @PathVariable Long appointmentId,
            @PathVariable Long patientId,
            @Valid @RequestBody RescheduleRequest request) {
        return ResponseEntity.ok(
                appointmentService.rescheduleAppointment(appointmentId, patientId, request));
    }

    @PutMapping("/{appointmentId}/cancel/patient/{patientId}")
    public ResponseEntity<AppointmentResponse> cancelAppointment(
            @PathVariable Long appointmentId,
            @PathVariable Long patientId,
            @Valid @RequestBody CancelRequest request) {
        return ResponseEntity.ok(
                appointmentService.cancelAppointment(appointmentId, patientId, request));
    }

    @GetMapping("/{appointmentId}")
    public ResponseEntity<AppointmentResponse> getAppointmentById(
            @PathVariable Long appointmentId) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(appointmentId));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<PagedResponse<AppointmentResponse>> getPatientAppointments(
            @PathVariable Long patientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
                appointmentService.getPatientAppointments(patientId, page, size));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<PagedResponse<AppointmentResponse>> getDoctorAppointments(
            @PathVariable Long doctorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
                appointmentService.getDoctorAppointments(doctorId, page, size));
    }
}