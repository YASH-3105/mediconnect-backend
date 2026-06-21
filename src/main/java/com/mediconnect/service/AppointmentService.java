package com.mediconnect.service;

import com.mediconnect.dto.request.AppointmentRequest;
import com.mediconnect.dto.request.CancelRequest;
import com.mediconnect.dto.request.RescheduleRequest;
import com.mediconnect.dto.response.AppointmentResponse;
import com.mediconnect.dto.response.PagedResponse;

public interface AppointmentService {
    AppointmentResponse bookAppointment(Long patientId, AppointmentRequest request);
    AppointmentResponse rescheduleAppointment(Long appointmentId, Long patientId, RescheduleRequest request);
    AppointmentResponse cancelAppointment(Long appointmentId, Long patientId, CancelRequest request);
    AppointmentResponse getAppointmentById(Long appointmentId);
    PagedResponse<AppointmentResponse> getPatientAppointments(Long patientId, int page, int size);
    PagedResponse<AppointmentResponse> getDoctorAppointments(Long doctorId, int page, int size);
}