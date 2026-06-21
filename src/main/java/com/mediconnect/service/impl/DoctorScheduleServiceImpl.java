package com.mediconnect.service.impl;

import com.mediconnect.dto.request.DoctorScheduleRequest;
import com.mediconnect.dto.request.LeaveBlockRequest;
import com.mediconnect.dto.response.DoctorScheduleResponse;
import com.mediconnect.dto.response.LeaveBlockResponse;
import com.mediconnect.entity.*;
import com.mediconnect.exception.BadRequestException;
import com.mediconnect.exception.ResourceNotFoundException;
import com.mediconnect.repository.*;
import com.mediconnect.service.DoctorScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorScheduleServiceImpl implements DoctorScheduleService {

    private final DoctorScheduleRepository scheduleRepository;
    private final LeaveBlockRepository leaveBlockRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public DoctorScheduleResponse createOrUpdateSchedule(Long doctorId, DoctorScheduleRequest request) {
        log.info("Creating/updating schedule for doctorId: {}", doctorId);

        DoctorProfile doctor = doctorProfileRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + doctorId));

        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new BadRequestException("Start time must be before end time.");
        }

        DoctorSchedule schedule = scheduleRepository
                .findByDoctorIdAndDayOfWeek(doctorId, request.getDayOfWeek())
                .orElse(DoctorSchedule.builder().doctor(doctor).build());

        schedule.setDayOfWeek(request.getDayOfWeek());
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());
        schedule.setSlotDurationMinutes(request.getSlotDurationMinutes());
        schedule.setBufferTimeMinutes(request.getBufferTimeMinutes() != null ? request.getBufferTimeMinutes() : 0);
        schedule.setDailyPatientCap(request.getDailyPatientCap());
        schedule.setActive(true);

        DoctorSchedule saved = scheduleRepository.save(schedule);
        return mapToResponse(saved);
    }

    @Override
    public List<DoctorScheduleResponse> getScheduleByDoctorId(Long doctorId) {
        return scheduleRepository.findByDoctorIdAndActiveTrue(doctorId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DoctorScheduleResponse deactivateSchedule(Long scheduleId) {
        DoctorSchedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Schedule not found with id: " + scheduleId));
        schedule.setActive(false);
        return mapToResponse(scheduleRepository.save(schedule));
    }

    @Override
    public LeaveBlockResponse addLeaveBlock(Long doctorId, LeaveBlockRequest request) {
        log.info("Adding leave block for doctorId: {} on date: {}", doctorId, request.getLeaveDate());

        DoctorProfile doctor = doctorProfileRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + doctorId));

        if (leaveBlockRepository.existsByDoctorIdAndLeaveDate(doctorId, request.getLeaveDate())) {
            throw new BadRequestException("Leave already marked for this date.");
        }

        LeaveBlock leaveBlock = LeaveBlock.builder()
                .doctor(doctor)
                .leaveDate(request.getLeaveDate())
                .reason(request.getReason())
                .build();

        LeaveBlock saved = leaveBlockRepository.save(leaveBlock);
        return mapLeaveToResponse(saved);
    }

    @Override
    public List<LeaveBlockResponse> getLeaveBlocks(Long doctorId) {
        return leaveBlockRepository.findByDoctorId(doctorId)
                .stream()
                .map(this::mapLeaveToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void removeLeaveBlock(Long leaveBlockId) {
        LeaveBlock leaveBlock = leaveBlockRepository.findById(leaveBlockId)
                .orElseThrow(() -> new ResourceNotFoundException("Leave block not found with id: " + leaveBlockId));
        leaveBlockRepository.delete(leaveBlock);
        log.info("Leave block removed: {}", leaveBlockId);
    }

    @Override
    public List<String> getAvailableSlots(Long doctorId, LocalDate date) {
        log.info("Getting available slots for doctorId: {} on date: {}", doctorId, date);

        // Check if doctor is on leave
        if (leaveBlockRepository.existsByDoctorIdAndLeaveDate(doctorId, date)) {
            return new ArrayList<>();
        }

        // Get schedule for that day
        Day day = Day.valueOf(date.getDayOfWeek().name());
        DoctorSchedule schedule = scheduleRepository
                .findByDoctorIdAndDayOfWeek(doctorId, day)
                .orElse(null);

        if (schedule == null || !schedule.isActive()) {
            return new ArrayList<>();
        }

        // Generate all slots
        List<String> allSlots = generateSlots(
                schedule.getStartTime(),
                schedule.getEndTime(),
                schedule.getSlotDurationMinutes(),
                schedule.getBufferTimeMinutes());

        // Get booked slots for that day
        List<com.mediconnect.entity.Appointment> booked =
                appointmentRepository.findByDoctorIdAndAppointmentDate(doctorId, date);

        List<String> bookedTimes = booked.stream()
                .filter(a -> a.getStatus() != AppointmentStatus.CANCELLED)
                .map(a -> a.getAppointmentTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                .collect(Collectors.toList());

        // Remove booked slots
        return allSlots.stream()
                .filter(slot -> !bookedTimes.contains(slot))
                .collect(Collectors.toList());
    }

    private List<String> generateSlots(LocalTime start, LocalTime end,
                                        int slotDuration, int buffer) {
        List<String> slots = new ArrayList<>();
        LocalTime current = start;
        int step = slotDuration + (buffer > 0 ? buffer : 0);

        while (current.plusMinutes(slotDuration).compareTo(end) <= 0) {
            slots.add(current.format(DateTimeFormatter.ofPattern("HH:mm")));
            current = current.plusMinutes(step);
        }
        return slots;
    }

    private DoctorScheduleResponse mapToResponse(DoctorSchedule s) {
        return DoctorScheduleResponse.builder()
                .id(s.getId())
                .doctorId(s.getDoctor().getId())
                .doctorName(s.getDoctor().getUser().getFullName())
                .dayOfWeek(s.getDayOfWeek())
                .startTime(s.getStartTime())
                .endTime(s.getEndTime())
                .slotDurationMinutes(s.getSlotDurationMinutes())
                .bufferTimeMinutes(s.getBufferTimeMinutes())
                .dailyPatientCap(s.getDailyPatientCap())
                .active(s.isActive())
                .createdAt(s.getCreatedAt())
                .updatedAt(s.getUpdatedAt())
                .build();
    }

    private LeaveBlockResponse mapLeaveToResponse(LeaveBlock l) {
        return LeaveBlockResponse.builder()
                .id(l.getId())
                .doctorId(l.getDoctor().getId())
                .doctorName(l.getDoctor().getUser().getFullName())
                .leaveDate(l.getLeaveDate())
                .reason(l.getReason())
                .createdAt(l.getCreatedAt())
                .build();
    }
}