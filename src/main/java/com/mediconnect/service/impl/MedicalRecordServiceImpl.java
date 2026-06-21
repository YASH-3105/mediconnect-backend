package com.mediconnect.service.impl;

import com.mediconnect.dto.request.MedicalRecordRequest;
import com.mediconnect.dto.response.MedicalRecordResponse;
import com.mediconnect.dto.response.PagedResponse;
import com.mediconnect.entity.*;
import com.mediconnect.exception.BadRequestException;
import com.mediconnect.exception.ResourceNotFoundException;
import com.mediconnect.repository.MedicalRecordRepository;
import com.mediconnect.repository.UserRepository;
import com.mediconnect.service.MedicalRecordService;
import com.mediconnect.util.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;

    @Value("${app.share.token.expiry.hours}")
    private int shareTokenExpiryHours;

    @Override
    public MedicalRecordResponse uploadRecord(Long patientId, MultipartFile file,
                                               MedicalRecordRequest request) {
        log.info("Uploading medical record for patientId: {}", patientId);

        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Patient not found with id: " + patientId));

        String filePath = fileStorageService.storeFile(file, patientId);

        MedicalRecord record = MedicalRecord.builder()
                .patient(patient)
                .fileName(UUID.randomUUID().toString())
                .originalFileName(file.getOriginalFilename())
                .fileType(file.getContentType())
                .fileSize(file.getSize())
                .filePath(filePath)
                .category(request.getCategory())
                .doctorName(request.getDoctorName())
                .labName(request.getLabName())
                .recordDate(request.getRecordDate())
                .notes(request.getNotes())
                .deleted(false)
                .build();

        MedicalRecord saved = medicalRecordRepository.save(record);
        log.info("Medical record uploaded with id: {}", saved.getId());

        return mapToResponse(saved);
    }

    @Override
    public MedicalRecordResponse getRecordById(Long recordId) {
        MedicalRecord record = medicalRecordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Record not found with id: " + recordId));
        return mapToResponse(record);
    }

    @Override
    public PagedResponse<MedicalRecordResponse> getPatientRecords(
            Long patientId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<MedicalRecord> result =
                medicalRecordRepository.findByPatientIdAndDeletedFalse(patientId, pageable);
        return buildPagedResponse(result);
    }

    @Override
    public PagedResponse<MedicalRecordResponse> getPatientRecordsByCategory(
            Long patientId, RecordCategory category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<MedicalRecord> result =
                medicalRecordRepository.findByPatientIdAndCategoryAndDeletedFalse(
                        patientId, category, pageable);
        return buildPagedResponse(result);
    }

    @Override
    public MedicalRecordResponse generateShareToken(Long recordId) {
        MedicalRecord record = medicalRecordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Record not found with id: " + recordId));

        record.setShareToken(UUID.randomUUID().toString());
        record.setShareTokenExpiry(LocalDateTime.now().plusHours(shareTokenExpiryHours));

        return mapToResponse(medicalRecordRepository.save(record));
    }

    @Override
    public MedicalRecordResponse getRecordByShareToken(String token) {
        MedicalRecord record = medicalRecordRepository.findByShareToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid share token."));

        if (record.getShareTokenExpiry() != null &&
                record.getShareTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Share token has expired.");
        }

        return mapToResponse(record);
    }

    @Override
    public void deleteRecord(Long recordId, Long patientId) {
        MedicalRecord record = medicalRecordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Record not found with id: " + recordId));

        if (!record.getPatient().getId().equals(patientId)) {
            throw new BadRequestException(
                    "You are not authorized to delete this record.");
        }

        record.setDeleted(true);
        record.setDeletedAt(LocalDateTime.now());
        medicalRecordRepository.save(record);
        log.info("Medical record soft-deleted: {}", recordId);
    }

    private PagedResponse<MedicalRecordResponse> buildPagedResponse(
            Page<MedicalRecord> page) {
        List<MedicalRecordResponse> content = page.getContent()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return PagedResponse.<MedicalRecordResponse>builder()
                .content(content)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    private MedicalRecordResponse mapToResponse(MedicalRecord r) {
        return MedicalRecordResponse.builder()
                .id(r.getId())
                .patientId(r.getPatient().getId())
                .patientName(r.getPatient().getFullName())
                .fileName(r.getFileName())
                .originalFileName(r.getOriginalFileName())
                .fileType(r.getFileType())
                .fileSize(r.getFileSize())
                .category(r.getCategory())
                .doctorName(r.getDoctorName())
                .labName(r.getLabName())
                .recordDate(r.getRecordDate())
                .notes(r.getNotes())
                .shareToken(r.getShareToken())
                .shareTokenExpiry(r.getShareTokenExpiry())
                .createdAt(r.getCreatedAt())
                .updatedAt(r.getUpdatedAt())
                .build();
    }
}