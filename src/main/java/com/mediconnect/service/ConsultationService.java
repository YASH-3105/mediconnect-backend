package com.mediconnect.service;

import com.mediconnect.dto.request.ConsultationNotesRequest;
import com.mediconnect.dto.response.ConsultationResponse;
import com.mediconnect.dto.response.PagedResponse;

public interface ConsultationService {
    ConsultationResponse createConsultation(Long appointmentId);
    ConsultationResponse startVideoSession(Long consultationId);
    ConsultationResponse endVideoSession(Long consultationId);
    ConsultationResponse saveNotes(Long consultationId, ConsultationNotesRequest request);
    ConsultationResponse completeConsultation(Long consultationId);
    ConsultationResponse getConsultationById(Long consultationId);
    ConsultationResponse getConsultationByAppointmentId(Long appointmentId);
    PagedResponse<ConsultationResponse> getPatientConsultations(Long patientId, int page, int size);
    PagedResponse<ConsultationResponse> getDoctorConsultations(Long doctorId, int page, int size);
}