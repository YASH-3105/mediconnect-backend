package com.mediconnect.service.impl;

import com.mediconnect.dto.request.AppointmentRequest;
import com.mediconnect.dto.request.CancelRequest;
import com.mediconnect.dto.request.RescheduleRequest;
import com.mediconnect.dto.response.AppointmentResponse;
import com.mediconnect.dto.response.PagedResponse;
import com.mediconnect.entity.*;
import com.mediconnect.exception.BadRequestException;
import com.mediconnect.exception.ResourceNotFoundException;
import com.mediconnect.repository.AppointmentRepository;
import com.mediconnect.repository.DoctorProfileRepository;
import com.mediconnect.repository.UserRepository;
import com.mediconnect.service.AppointmentService;
import com.mediconnect.service.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final NotificationService notificationService;

    @Override
    public AppointmentResponse bookAppointment(Long patientId, AppointmentRequest request) {
        log.info("Booking appointment for patientId: {}", patientId);

        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));

        DoctorProfile doctor = doctorProfileRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id: " + request.getDoctorId()));

        if (!doctor.isApproved()) {
            throw new BadRequestException("This doctor is not yet approved for consultations.");
        }

        if (!doctor.isAcceptingNewPatients()) {
            throw new BadRequestException("This doctor is not accepting new patients.");
        }

        // Check slot conflict
        List<Appointment> conflicts = appointmentRepository.findConflictingAppointment(
                doctor.getId(), request.getAppointmentDate(), request.getAppointmentTime());

        if (!conflicts.isEmpty()) {
            throw new BadRequestException("This slot is already booked. Please choose a different time.");
        }

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentDate(request.getAppointmentDate())
                .appointmentTime(request.getAppointmentTime())
                .consultationType(request.getConsultationType())
                .fee(doctor.getConsultationFee())
                .notes(request.getNotes())
                .status(AppointmentStatus.CONFIRMED)
                .build();

        Appointment saved = appointmentRepository.save(appointment);
     // Notify patient
        notificationService.sendNotification(
                patient,
                "Appointment Confirmed",
                "Your appointment with " + doctor.getUser().getFullName() +
                " on " + request.getAppointmentDate() + " at " +
                request.getAppointmentTime() + " is confirmed.",
                NotificationType.APPOINTMENT_BOOKED,
                "/appointments/" + saved.getId()
        );

        // Notify doctor
        notificationService.sendNotification(
                doctor.getUser(),
                "New Appointment",
                "New appointment booked by " + patient.getFullName() +
                " on " + request.getAppointmentDate() + " at " +
                request.getAppointmentTime(),
                NotificationType.APPOINTMENT_BOOKED,
                "/appointments/" + saved.getId()
        );
        log.info("Appointment booked with reference: {}", saved.getAppointmentReference());

        return mapToResponse(saved);
    }

    @Override
    public AppointmentResponse rescheduleAppointment(
            Long appointmentId, Long patientId, RescheduleRequest request) {
        log.info("Rescheduling appointment: {}", appointmentId);

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));

        if (!appointment.getPatient().getId().equals(patientId)) {
            throw new BadRequestException("You are not authorized to reschedule this appointment.");
        }

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new BadRequestException("Cannot reschedule a cancelled appointment.");
        }

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new BadRequestException("Cannot reschedule a completed appointment.");
        }

        // Check new slot conflict
        List<Appointment> conflicts = appointmentRepository.findConflictingAppointment(
                appointment.getDoctor().getId(),
                request.getAppointmentDate(),
                request.getAppointmentTime());

        if (!conflicts.isEmpty()) {
            throw new BadRequestException("The new slot is already booked. Please choose a different time.");
        }

        appointment.setAppointmentDate(request.getAppointmentDate());
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setRescheduleReason(request.getRescheduleReason());
        appointment.setStatus(AppointmentStatus.RESCHEDULED);

        Appointment saved = appointmentRepository.save(appointment);
        log.info("Appointment rescheduled: {}", appointmentId);

        return mapToResponse(saved);
    }

    @Override
    public AppointmentResponse cancelAppointment(
            Long appointmentId, Long patientId, CancelRequest request) {
        log.info("Cancelling appointment: {}", appointmentId);

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));

        if (!appointment.getPatient().getId().equals(patientId)) {
            throw new BadRequestException("You are not authorized to cancel this appointment.");
        }

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new BadRequestException("Appointment is already cancelled.");
        }

        if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new BadRequestException("Cannot cancel a completed appointment.");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointment.setCancellationReason(request.getCancellationReason());

        Appointment saved = appointmentRepository.save(appointment);
        notificationService.sendNotification(
                appointment.getPatient(),
                "Appointment Cancelled",
                "Your appointment with " +
                appointment.getDoctor().getUser().getFullName() + " has been cancelled.",
                NotificationType.APPOINTMENT_CANCELLED,
                "/appointments/" + saved.getId()
        );
        log.info("Appointment cancelled: {}", appointmentId);

        return mapToResponse(saved);
    }

    @Override
    public AppointmentResponse getAppointmentById(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));
        return mapToResponse(appointment);
    }

    @Override
    public PagedResponse<AppointmentResponse> getPatientAppointments(
            Long patientId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("appointmentDate").descending());
        Page<Appointment> result = appointmentRepository.findByPatientId(patientId, pageable);
        return buildPagedResponse(result);
    }

    @Override
    public PagedResponse<AppointmentResponse> getDoctorAppointments(
            Long doctorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("appointmentDate").ascending());
        Page<Appointment> result = appointmentRepository.findByDoctorId(doctorId, pageable);
        return buildPagedResponse(result);
    }

    private PagedResponse<AppointmentResponse> buildPagedResponse(Page<Appointment> page) {
        List<AppointmentResponse> content = page.getContent()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return PagedResponse.<AppointmentResponse>builder()
                .content(content)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    private AppointmentResponse mapToResponse(Appointment a) {
        return AppointmentResponse.builder()
                .id(a.getId())
                .appointmentReference(a.getAppointmentReference())
                .patientId(a.getPatient().getId())
                .patientName(a.getPatient().getFullName())
                .patientEmail(a.getPatient().getEmail())
                .doctorId(a.getDoctor().getId())
                .doctorName(a.getDoctor().getUser().getFullName())
                .doctorEmail(a.getDoctor().getUser().getEmail())
                .specialization(a.getDoctor().getSpecialization())
                .clinicName(a.getDoctor().getClinicName())
                .clinicAddress(a.getDoctor().getClinicAddress())
                .appointmentDate(a.getAppointmentDate())
                .appointmentTime(a.getAppointmentTime())
                .consultationType(a.getConsultationType())
                .status(a.getStatus())
                .fee(a.getFee())
                .notes(a.getNotes())
                .cancellationReason(a.getCancellationReason())
                .rescheduleReason(a.getRescheduleReason())
                .createdAt(a.getCreatedAt())
                .updatedAt(a.getUpdatedAt())
                .build();
    }
}