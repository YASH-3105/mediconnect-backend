package com.mediconnect.service.impl;

import com.mediconnect.dto.request.HealthVitalRequest;
import com.mediconnect.dto.response.HealthVitalResponse;
import com.mediconnect.dto.response.PagedResponse;
import com.mediconnect.entity.*;
import com.mediconnect.exception.BadRequestException;
import com.mediconnect.exception.ResourceNotFoundException;
import com.mediconnect.repository.*;
import com.mediconnect.service.HealthVitalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HealthVitalServiceImpl implements HealthVitalService {

    private final HealthVitalRepository vitalRepository;
    private final UserRepository userRepository;

    private static final Map<VitalType, double[]> NORMAL_RANGES = Map.of(
            VitalType.BLOOD_PRESSURE_SYSTOLIC, new double[]{90, 120},
            VitalType.BLOOD_PRESSURE_DIASTOLIC, new double[]{60, 80},
            VitalType.HEART_RATE, new double[]{60, 100},
            VitalType.BLOOD_GLUCOSE, new double[]{70, 140},
            VitalType.WEIGHT, new double[]{30, 200},
            VitalType.SPO2, new double[]{95, 100},
            VitalType.TEMPERATURE, new double[]{36.1, 37.2}
    );

    private static final Map<VitalType, String> NORMAL_RANGE_LABELS = Map.of(
            VitalType.BLOOD_PRESSURE_SYSTOLIC, "90-120 mmHg",
            VitalType.BLOOD_PRESSURE_DIASTOLIC, "60-80 mmHg",
            VitalType.HEART_RATE, "60-100 bpm",
            VitalType.BLOOD_GLUCOSE, "70-140 mg/dL",
            VitalType.WEIGHT, "30-200 kg",
            VitalType.SPO2, "95-100%",
            VitalType.TEMPERATURE, "36.1-37.2°C"
    );

    @Override
    public HealthVitalResponse logVital(Long patientId, HealthVitalRequest request) {
        log.info("Logging vital {} for patientId: {}", request.getVitalType(), patientId);

        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Patient not found with id: " + patientId));

        double[] range = NORMAL_RANGES.get(request.getVitalType());
        boolean abnormal = range != null &&
                (request.getValue() < range[0] || request.getValue() > range[1]);

        HealthVital vital = HealthVital.builder()
                .patient(patient)
                .vitalType(request.getVitalType())
                .value(request.getValue())
                .unit(request.getUnit())
                .notes(request.getNotes())
                .abnormal(abnormal)
                .recordedAt(request.getRecordedAt())
                .build();

        return mapToResponse(vitalRepository.save(vital));
    }

    @Override
    public PagedResponse<HealthVitalResponse> getPatientVitals(
            Long patientId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<HealthVital> result =
                vitalRepository.findByPatientIdOrderByRecordedAtDesc(
                        patientId, pageable);

        List<HealthVitalResponse> content = result.getContent()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return PagedResponse.<HealthVitalResponse>builder()
                .content(content)
                .pageNumber(result.getNumber())
                .pageSize(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .last(result.isLast())
                .build();
    }

    @Override
    public List<HealthVitalResponse> getVitalTrend(Long patientId, VitalType type,
                                                     LocalDateTime start,
                                                     LocalDateTime end) {
        return vitalRepository
                .findByPatientIdAndVitalTypeAndRecordedAtBetweenOrderByRecordedAtAsc(
                        patientId, type, start, end)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteVital(Long vitalId, Long patientId) {
        HealthVital vital = vitalRepository.findById(vitalId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Vital not found with id: " + vitalId));

        if (!vital.getPatient().getId().equals(patientId)) {
            throw new BadRequestException(
                    "You are not authorized to delete this vital.");
        }

        vitalRepository.delete(vital);
    }

    private HealthVitalResponse mapToResponse(HealthVital v) {
        return HealthVitalResponse.builder()
                .id(v.getId())
                .patientId(v.getPatient().getId())
                .patientName(v.getPatient().getFullName())
                .vitalType(v.getVitalType())
                .value(v.getValue())
                .unit(v.getUnit())
                .notes(v.getNotes())
                .abnormal(v.isAbnormal())
                .normalRange(NORMAL_RANGE_LABELS.getOrDefault(
                        v.getVitalType(), "N/A"))
                .recordedAt(v.getRecordedAt())
                .createdAt(v.getCreatedAt())
                .build();
    }
}