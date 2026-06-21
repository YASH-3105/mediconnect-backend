package com.mediconnect.service;

import com.mediconnect.dto.request.MedicalRecordRequest;
import com.mediconnect.dto.response.MedicalRecordResponse;
import com.mediconnect.dto.response.PagedResponse;
import com.mediconnect.entity.RecordCategory;
import org.springframework.web.multipart.MultipartFile;

public interface MedicalRecordService {
    MedicalRecordResponse uploadRecord(Long patientId, MultipartFile file,
                                       MedicalRecordRequest request);
    MedicalRecordResponse getRecordById(Long recordId);
    PagedResponse<MedicalRecordResponse> getPatientRecords(Long patientId,
                                                            int page, int size);
    PagedResponse<MedicalRecordResponse> getPatientRecordsByCategory(
            Long patientId, RecordCategory category, int page, int size);
    MedicalRecordResponse generateShareToken(Long recordId);
    MedicalRecordResponse getRecordByShareToken(String token);
    void deleteRecord(Long recordId, Long patientId);
}