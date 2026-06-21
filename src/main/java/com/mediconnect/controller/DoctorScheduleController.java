package com.mediconnect.controller;

import com.mediconnect.dto.request.DoctorScheduleRequest;
import com.mediconnect.dto.request.LeaveBlockRequest;
import com.mediconnect.dto.response.DoctorScheduleResponse;
import com.mediconnect.dto.response.LeaveBlockResponse;
import com.mediconnect.service.DoctorScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
public class DoctorScheduleController {

    private final DoctorScheduleService scheduleService;

    @PostMapping("/doctor/{doctorId}")
    public ResponseEntity<DoctorScheduleResponse> createOrUpdateSchedule(
            @PathVariable Long doctorId,
            @Valid @RequestBody DoctorScheduleRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(scheduleService.createOrUpdateSchedule(doctorId, request));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<DoctorScheduleResponse>> getSchedule(
            @PathVariable Long doctorId) {
        return ResponseEntity.ok(scheduleService.getScheduleByDoctorId(doctorId));
    }

    @PatchMapping("/{scheduleId}/deactivate")
    public ResponseEntity<DoctorScheduleResponse> deactivateSchedule(
            @PathVariable Long scheduleId) {
        return ResponseEntity.ok(scheduleService.deactivateSchedule(scheduleId));
    }

    @PostMapping("/doctor/{doctorId}/leave")
    public ResponseEntity<LeaveBlockResponse> addLeave(
            @PathVariable Long doctorId,
            @Valid @RequestBody LeaveBlockRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(scheduleService.addLeaveBlock(doctorId, request));
    }

    @GetMapping("/doctor/{doctorId}/leave")
    public ResponseEntity<List<LeaveBlockResponse>> getLeaves(
            @PathVariable Long doctorId) {
        return ResponseEntity.ok(scheduleService.getLeaveBlocks(doctorId));
    }

    @DeleteMapping("/leave/{leaveBlockId}")
    public ResponseEntity<Void> removeLeave(@PathVariable Long leaveBlockId) {
        scheduleService.removeLeaveBlock(leaveBlockId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/doctor/{doctorId}/available-slots")
    public ResponseEntity<List<String>> getAvailableSlots(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(scheduleService.getAvailableSlots(doctorId, date));
    }
}