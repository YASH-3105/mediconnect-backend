package com.mediconnect.service;

import com.mediconnect.dto.request.HealthVitalRequest;
import com.mediconnect.dto.response.HealthVitalResponse;
import com.mediconnect.dto.response.PagedResponse;
import com.mediconnect.entity.VitalType;
import java.time.LocalDateTime;
import java.util.List;

public interface HealthVitalService {
    HealthVitalResponse logVital(Long patientId, HealthVitalRequest request);
    PagedResponse<HealthVitalResponse> getPatientVitals(Long patientId, int page, int size);
    List<HealthVitalResponse> getVitalTrend(Long patientId, VitalType type,
                                              LocalDateTime start, LocalDateTime end);
    void deleteVital(Long vitalId, Long patientId);
}