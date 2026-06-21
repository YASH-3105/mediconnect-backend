package com.mediconnect.service.impl;

import com.mediconnect.dto.request.PrescriptionItemRequest;
import com.mediconnect.dto.request.PrescriptionRequest;
import com.mediconnect.dto.response.PagedResponse;
import com.mediconnect.dto.response.PrescriptionItemResponse;
import com.mediconnect.dto.response.PrescriptionResponse;
import com.mediconnect.entity.*;
import com.mediconnect.exception.ResourceNotFoundException;
import com.mediconnect.repository.*;
import com.mediconnect.service.PrescriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;
    private final ConsultationRepository consultationRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final UserRepository userRepository;
    private final PatientProfileRepository patientProfileRepository;
    private final MedicineRepository medicineRepository;

    @Override
    public PrescriptionResponse createPrescription(Long doctorId, PrescriptionRequest request) {
        log.info("Creating prescription by doctorId: {}", doctorId);

        DoctorProfile doctor = doctorProfileRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Doctor not found with id: " + doctorId));

        Consultation consultation = consultationRepository.findById(request.getConsultationId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Consultation not found with id: " + request.getConsultationId()));

        User patient = userRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Patient not found with id: " + request.getPatientId()));

        // Check allergies
        List<String> patientAllergies = getPatientAllergies(request.getPatientId());

        // Build prescription items with allergy check
        List<PrescriptionItem> items = request.getItems().stream()
                .map(itemReq -> buildPrescriptionItem(itemReq, patientAllergies))
                .collect(Collectors.toList());

        Prescription prescription = Prescription.builder()
                .consultation(consultation)
                .doctor(doctor)
                .patient(patient)
                .repeatPrescription(request.isRepeatPrescription())
                .validUntil(request.getValidUntil())
                .additionalInstructions(request.getAdditionalInstructions())
                .build();

        Prescription saved = prescriptionRepository.save(prescription);

        // Set prescription reference on items
        items.forEach(item -> item.setPrescription(saved));
        saved.setItems(items);
        prescriptionRepository.save(saved);

        log.info("Prescription created: {}", saved.getPrescriptionNumber());
        return mapToResponse(saved);
    }

    @Override
    public PrescriptionResponse getPrescriptionById(Long prescriptionId) {
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Prescription not found with id: " + prescriptionId));
        return mapToResponse(prescription);
    }

    @Override
    public PagedResponse<PrescriptionResponse> getPatientPrescriptions(
            Long patientId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Prescription> result = prescriptionRepository.findByPatientId(patientId, pageable);
        return buildPagedResponse(result);
    }

    @Override
    public PagedResponse<PrescriptionResponse> getDoctorPrescriptions(
            Long doctorId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Prescription> result = prescriptionRepository.findByDoctorId(doctorId, pageable);
        return buildPagedResponse(result);
    }

    @Override
    public List<PrescriptionResponse> getConsultationPrescriptions(Long consultationId) {
        return prescriptionRepository.findByConsultationId(consultationId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<Medicine> searchMedicines(String query) {
        return medicineRepository.findByNameContainingIgnoreCaseAndActiveTrue(query);
    }

    private List<String> getPatientAllergies(Long patientId) {
        return patientProfileRepository.findByUserId(patientId)
                .map(profile -> {
                    if (profile.getAllergies() == null) return List.<String>of();
                    return List.of(profile.getAllergies().toLowerCase().split(","));
                })
                .orElse(List.of());
    }

    private PrescriptionItem buildPrescriptionItem(
            PrescriptionItemRequest req, List<String> allergies) {
        boolean flagged = allergies.stream()
                .anyMatch(a -> req.getMedicineName().toLowerCase().contains(a.trim()));

        return PrescriptionItem.builder()
                .medicineName(req.getMedicineName())
                .dosage(req.getDosage())
                .frequency(req.getFrequency())
                .duration(req.getDuration())
                .instructions(req.getInstructions())
                .allergyFlagged(flagged)
                .build();
    }

    private PagedResponse<PrescriptionResponse> buildPagedResponse(Page<Prescription> page) {
        List<PrescriptionResponse> content = page.getContent()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return PagedResponse.<PrescriptionResponse>builder()
                .content(content)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    private PrescriptionResponse mapToResponse(Prescription p) {
        List<PrescriptionItemResponse> itemResponses = p.getItems() == null
                ? List.of()
                : p.getItems().stream()
                        .map(item -> PrescriptionItemResponse.builder()
                                .id(item.getId())
                                .medicineName(item.getMedicineName())
                                .dosage(item.getDosage())
                                .frequency(item.getFrequency())
                                .duration(item.getDuration())
                                .instructions(item.getInstructions())
                                .allergyFlagged(item.isAllergyFlagged())
                                .build())
                        .collect(Collectors.toList());

        return PrescriptionResponse.builder()
                .id(p.getId())
                .prescriptionNumber(p.getPrescriptionNumber())
                .consultationId(p.getConsultation().getId())
                .doctorId(p.getDoctor().getId())
                .doctorName(p.getDoctor().getUser().getFullName())
                .specialization(p.getDoctor().getSpecialization().name())
                .patientId(p.getPatient().getId())
                .patientName(p.getPatient().getFullName())
                .items(itemResponses)
                .repeatPrescription(p.isRepeatPrescription())
                .validUntil(p.getValidUntil())
                .additionalInstructions(p.getAdditionalInstructions())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .build();
    }
}