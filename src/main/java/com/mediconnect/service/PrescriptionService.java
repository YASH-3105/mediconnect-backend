package com.mediconnect.service;

import com.mediconnect.dto.request.PrescriptionRequest;
import com.mediconnect.dto.response.PrescriptionResponse;
import com.mediconnect.dto.response.PagedResponse;
import com.mediconnect.entity.Medicine;
import java.util.List;

public interface PrescriptionService {
    PrescriptionResponse createPrescription(Long doctorId, PrescriptionRequest request);
    PrescriptionResponse getPrescriptionById(Long prescriptionId);
    PagedResponse<PrescriptionResponse> getPatientPrescriptions(Long patientId, int page, int size);
    PagedResponse<PrescriptionResponse> getDoctorPrescriptions(Long doctorId, int page, int size);
    List<PrescriptionResponse> getConsultationPrescriptions(Long consultationId);
    List<Medicine> searchMedicines(String query);
}