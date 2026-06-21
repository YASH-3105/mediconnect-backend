package com.mediconnect.service.impl;

import com.mediconnect.dto.request.ConsultationNotesRequest;
import com.mediconnect.dto.response.ConsultationResponse;
import com.mediconnect.dto.response.PagedResponse;
import com.mediconnect.entity.*;
import com.mediconnect.exception.BadRequestException;
import com.mediconnect.exception.ResourceNotFoundException;
import com.mediconnect.repository.AppointmentRepository;
import com.mediconnect.repository.ConsultationRepository;
import com.mediconnect.service.ConsultationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsultationServiceImpl implements ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public ConsultationResponse createConsultation(Long appointmentId) {
        log.info("Creating consultation for appointmentId: {}", appointmentId);

        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appointment not found with id: " + appointmentId));

        if (consultationRepository.findByAppointmentId(appointmentId).isPresent()) {
            throw new BadRequestException("Consultation already exists for this appointment.");
        }

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new BadRequestException("Cannot create consultation for a cancelled appointment.");
        }

        // Generate Jitsi room ID
        String sessionId = "mediconnect-" + UUID.randomUUID().toString().substring(0, 8);

        Consultation consultation = Consultation.builder()
                .appointment(appointment)
                .status(ConsultationStatus.SCHEDULED)
                .videoSessionId(sessionId)
                .build();

        Consultation saved = consultationRepository.save(consultation);
        log.info("Consultation created with id: {}", saved.getId());

        return mapToResponse(saved);
    }

    @Override
    public ConsultationResponse startVideoSession(Long consultationId) {
        log.info("Starting video session for consultationId: {}", consultationId);

        Consultation consultation = getConsultationEntity(consultationId);

        if (consultation.getStatus() == ConsultationStatus.COMPLETED) {
            throw new BadRequestException("Consultation is already completed.");
        }

        consultation.setStatus(ConsultationStatus.IN_PROGRESS);
        consultation.setSessionStartTime(LocalDateTime.now());

        return mapToResponse(consultationRepository.save(consultation));
    }

    @Override
    public ConsultationResponse endVideoSession(Long consultationId) {
        log.info("Ending video session for consultationId: {}", consultationId);

        Consultation consultation = getConsultationEntity(consultationId);

        if (consultation.getStatus() != ConsultationStatus.IN_PROGRESS) {
            throw new BadRequestException("Consultation is not in progress.");
        }

        LocalDateTime endTime = LocalDateTime.now();
        consultation.setSessionEndTime(endTime);

        if (consultation.getSessionStartTime() != null) {
            long duration = ChronoUnit.MINUTES.between(
                    consultation.getSessionStartTime(), endTime);
            consultation.setDurationMinutes(duration);
        }

        return mapToResponse(consultationRepository.save(consultation));
    }

    @Override
    public ConsultationResponse saveNotes(Long consultationId, ConsultationNotesRequest request) {
        log.info("Saving notes for consultationId: {}", consultationId);

        Consultation consultation = getConsultationEntity(consultationId);

        consultation.setSymptoms(request.getSymptoms());
        consultation.setDiagnosis(request.getDiagnosis());
        consultation.setTreatmentPlan(request.getTreatmentPlan());
        consultation.setFollowUpDate(request.getFollowUpDate());
        consultation.setDoctorNotes(request.getDoctorNotes());

        return mapToResponse(consultationRepository.save(consultation));
    }

    @Override
    public ConsultationResponse completeConsultation(Long consultationId) {
        log.info("Completing consultationId: {}", consultationId);

        Consultation consultation = getConsultationEntity(consultationId);

        if (consultation.getDiagnosis() == null || consultation.getDiagnosis().isBlank()) {
            throw new BadRequestException("Please save consultation notes before completing.");
        }

        consultation.setStatus(ConsultationStatus.COMPLETED);

        // Mark appointment as completed
        Appointment appointment = consultation.getAppointment();
        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);

        return mapToResponse(consultationRepository.save(consultation));
    }

    @Override
    public ConsultationResponse getConsultationById(Long consultationId) {
        return mapToResponse(getConsultationEntity(consultationId));
    }

    @Override
    public ConsultationResponse getConsultationByAppointmentId(Long appointmentId) {
        Consultation consultation = consultationRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Consultation not found for appointmentId: " + appointmentId));
        return mapToResponse(consultation);
    }

    @Override
    public PagedResponse<ConsultationResponse> getPatientConsultations(
            Long patientId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Consultation> result = consultationRepository.findByPatientId(patientId, pageable);
        return buildPagedResponse(result);
    }

    @Override
    public PagedResponse<ConsultationResponse> getDoctorConsultations(
            Long doctorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Consultation> result = consultationRepository.findByDoctorId(doctorId, pageable);
        return buildPagedResponse(result);
    }

    private Consultation getConsultationEntity(Long consultationId) {
        return consultationRepository.findById(consultationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Consultation not found with id: " + consultationId));
    }

    private PagedResponse<ConsultationResponse> buildPagedResponse(Page<Consultation> page) {
        List<ConsultationResponse> content = page.getContent()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return PagedResponse.<ConsultationResponse>builder()
                .content(content)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    private ConsultationResponse mapToResponse(Consultation c) {
        String videoJoinUrl = "https://meet.jit.si/" + c.getVideoSessionId();

        return ConsultationResponse.builder()
                .id(c.getId())
                .appointmentId(c.getAppointment().getId())
                .appointmentReference(c.getAppointment().getAppointmentReference())
                .patientId(c.getAppointment().getPatient().getId())
                .patientName(c.getAppointment().getPatient().getFullName())
                .doctorId(c.getAppointment().getDoctor().getId())
                .doctorName(c.getAppointment().getDoctor().getUser().getFullName())
                .specialization(c.getAppointment().getDoctor().getSpecialization().name())
                .status(c.getStatus())
                .symptoms(c.getSymptoms())
                .diagnosis(c.getDiagnosis())
                .treatmentPlan(c.getTreatmentPlan())
                .followUpDate(c.getFollowUpDate())
                .doctorNotes(c.getDoctorNotes())
                .videoSessionId(c.getVideoSessionId())
                .videoJoinUrl(videoJoinUrl)
                .sessionStartTime(c.getSessionStartTime())
                .sessionEndTime(c.getSessionEndTime())
                .durationMinutes(c.getDurationMinutes())
                .createdAt(c.getCreatedAt())
                .updatedAt(c.getUpdatedAt())
                .build();
    }
}