package com.mediconnect.service.impl;

import com.mediconnect.dto.request.DelegationRequest;
import com.mediconnect.dto.response.DelegationResponse;
import com.mediconnect.entity.*;
import com.mediconnect.exception.BadRequestException;
import com.mediconnect.exception.ResourceNotFoundException;
import com.mediconnect.repository.*;
import com.mediconnect.service.DelegationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DelegationServiceImpl implements DelegationService {

    private final DelegationRepository delegationRepository;
    private final DoctorProfileRepository doctorProfileRepository;
    private final UserRepository userRepository;

    @Override
    public DelegationResponse createDelegation(Long doctorId,
                                                DelegationRequest request) {
        log.info("Creating delegation for doctorId: {}", doctorId);

        DoctorProfile doctor = doctorProfileRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Doctor not found with id: " + doctorId));

        User coordinator = userRepository.findById(request.getCoordinatorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Coordinator not found with id: " + request.getCoordinatorId()));

        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new BadRequestException("Start date must be before end date.");
        }

        Delegation delegation = Delegation.builder()
                .doctor(doctor)
                .coordinator(coordinator)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .reason(request.getReason())
                .active(true)
                .build();

        return mapToResponse(delegationRepository.save(delegation));
    }

    @Override
    public List<DelegationResponse> getActiveDelegations(Long doctorId) {
        return delegationRepository.findByDoctorIdAndActiveTrue(doctorId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DelegationResponse revokeDelegation(Long delegationId) {
        log.info("Revoking delegation: {}", delegationId);
        Delegation delegation = delegationRepository.findById(delegationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Delegation not found with id: " + delegationId));
        delegation.setActive(false);
        return mapToResponse(delegationRepository.save(delegation));
    }

    private DelegationResponse mapToResponse(Delegation d) {
        return DelegationResponse.builder()
                .id(d.getId())
                .doctorId(d.getDoctor().getId())
                .doctorName(d.getDoctor().getUser().getFullName())
                .coordinatorId(d.getCoordinator().getId())
                .coordinatorName(d.getCoordinator().getFullName())
                .startDate(d.getStartDate())
                .endDate(d.getEndDate())
                .active(d.isActive())
                .reason(d.getReason())
                .createdAt(d.getCreatedAt())
                .updatedAt(d.getUpdatedAt())
                .build();
    }
}