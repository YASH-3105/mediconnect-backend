package com.mediconnect.service.impl;

import com.mediconnect.dto.request.DispenseRequest;
import com.mediconnect.dto.request.PharmacyInventoryRequest;
import com.mediconnect.dto.response.DispensingRecordResponse;
import com.mediconnect.dto.response.PagedResponse;
import com.mediconnect.dto.response.PharmacyInventoryResponse;
import com.mediconnect.entity.*;
import com.mediconnect.exception.BadRequestException;
import com.mediconnect.exception.ResourceNotFoundException;
import com.mediconnect.repository.*;
import com.mediconnect.service.NotificationService;
import com.mediconnect.service.PharmacyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PharmacyServiceImpl implements PharmacyService {

    private final PharmacyInventoryRepository inventoryRepository;
    private final DispensingRecordRepository dispensingRecordRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Override
    public PharmacyInventoryResponse addInventory(Long pharmacyUserId,
                                                   PharmacyInventoryRequest request) {
        log.info("Adding inventory for pharmacyUserId: {}", pharmacyUserId);

        User pharmacyUser = userRepository.findById(pharmacyUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pharmacy user not found with id: " + pharmacyUserId));

        PharmacyInventory inventory = PharmacyInventory.builder()
                .pharmacyUser(pharmacyUser)
                .medicineName(request.getMedicineName())
                .genericName(request.getGenericName())
                .manufacturer(request.getManufacturer())
                .stockQuantity(request.getStockQuantity())
                .reorderThreshold(request.getReorderThreshold())
                .unitPrice(request.getUnitPrice())
                .active(true)
                .build();

        return mapInventoryToResponse(inventoryRepository.save(inventory));
    }

    @Override
    public PharmacyInventoryResponse updateInventory(Long inventoryId,
                                                      PharmacyInventoryRequest request) {
        PharmacyInventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory item not found with id: " + inventoryId));

        inventory.setMedicineName(request.getMedicineName());
        inventory.setGenericName(request.getGenericName());
        inventory.setManufacturer(request.getManufacturer());
        inventory.setStockQuantity(request.getStockQuantity());
        inventory.setReorderThreshold(request.getReorderThreshold());
        inventory.setUnitPrice(request.getUnitPrice());

        return mapInventoryToResponse(inventoryRepository.save(inventory));
    }

    @Override
    public PharmacyInventoryResponse updateStock(Long inventoryId, Integer quantity) {
        PharmacyInventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory item not found with id: " + inventoryId));

        inventory.setStockQuantity(quantity);
        PharmacyInventory saved = inventoryRepository.save(inventory);

        if (saved.getStockQuantity() <= saved.getReorderThreshold()) {
            log.warn("Low stock alert for medicine: {}", saved.getMedicineName());
        }

        return mapInventoryToResponse(saved);
    }

    @Override
    public void deleteInventory(Long inventoryId) {
        PharmacyInventory inventory = inventoryRepository.findById(inventoryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Inventory item not found with id: " + inventoryId));
        inventory.setActive(false);
        inventoryRepository.save(inventory);
    }

    @Override
    public PagedResponse<PharmacyInventoryResponse> getInventory(
            Long pharmacyUserId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by("medicineName").ascending());
        Page<PharmacyInventory> result =
                inventoryRepository.findByPharmacyUserIdAndActiveTrue(
                        pharmacyUserId, pageable);

        List<PharmacyInventoryResponse> content = result.getContent()
                .stream()
                .map(this::mapInventoryToResponse)
                .collect(Collectors.toList());

        return PagedResponse.<PharmacyInventoryResponse>builder()
                .content(content)
                .pageNumber(result.getNumber())
                .pageSize(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .last(result.isLast())
                .build();
    }

    @Override
    public List<PharmacyInventoryResponse> getLowStockItems(Long pharmacyUserId) {
        return inventoryRepository.findLowStockItems(pharmacyUserId)
                .stream()
                .map(this::mapInventoryToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DispensingRecordResponse dispensePrescription(Long pharmacyUserId,
                                                          Long prescriptionId,
                                                          DispenseRequest request) {
        log.info("Dispensing prescription: {} by pharmacyUserId: {}",
                prescriptionId, pharmacyUserId);

        User pharmacyUser = userRepository.findById(pharmacyUserId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Pharmacy user not found with id: " + pharmacyUserId));

        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Prescription not found with id: " + prescriptionId));

        DispensingRecord record = dispensingRecordRepository
                .findByPrescriptionId(prescriptionId)
                .orElse(DispensingRecord.builder()
                        .prescription(prescription)
                        .pharmacyUser(pharmacyUser)
                        .patient(prescription.getPatient())
                        .build());

        record.setStatus(request.getStatus());
        record.setNotes(request.getNotes());
        record.setRejectionReason(request.getRejectionReason());

        DispensingRecord saved = dispensingRecordRepository.save(record);

        // Notify patient
        String statusMsg = request.getStatus() == DispenseStatus.DISPENSED
                ? "is ready for pickup"
                : "status updated to " + request.getStatus().name();

        notificationService.sendNotification(
                prescription.getPatient(),
                "Prescription Update",
                "Your prescription " + prescription.getPrescriptionNumber() +
                " " + statusMsg + ".",
                NotificationType.PRESCRIPTION_READY,
                "/prescriptions/" + prescriptionId
        );

        return mapDispensingToResponse(saved);
    }

    @Override
    public PagedResponse<DispensingRecordResponse> getDispensingQueue(
            Long pharmacyUserId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DispensingRecord> result =
                dispensingRecordRepository
                        .findByPharmacyUserIdOrderByCreatedAtDesc(
                                pharmacyUserId, pageable);

        List<DispensingRecordResponse> content = result.getContent()
                .stream()
                .map(this::mapDispensingToResponse)
                .collect(Collectors.toList());

        return PagedResponse.<DispensingRecordResponse>builder()
                .content(content)
                .pageNumber(result.getNumber())
                .pageSize(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .last(result.isLast())
                .build();
    }

    @Override
    public DispensingRecordResponse getDispensingRecord(Long recordId) {
        DispensingRecord record = dispensingRecordRepository.findById(recordId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Dispensing record not found with id: " + recordId));
        return mapDispensingToResponse(record);
    }

    private PharmacyInventoryResponse mapInventoryToResponse(PharmacyInventory i) {
        return PharmacyInventoryResponse.builder()
                .id(i.getId())
                .pharmacyUserId(i.getPharmacyUser().getId())
                .pharmacyName(i.getPharmacyUser().getFullName())
                .medicineName(i.getMedicineName())
                .genericName(i.getGenericName())
                .manufacturer(i.getManufacturer())
                .stockQuantity(i.getStockQuantity())
                .reorderThreshold(i.getReorderThreshold())
                .unitPrice(i.getUnitPrice())
                .active(i.isActive())
                .lowStock(i.getStockQuantity() <= i.getReorderThreshold())
                .createdAt(i.getCreatedAt())
                .updatedAt(i.getUpdatedAt())
                .build();
    }

    private DispensingRecordResponse mapDispensingToResponse(DispensingRecord d) {
        return DispensingRecordResponse.builder()
                .id(d.getId())
                .prescriptionId(d.getPrescription().getId())
                .prescriptionNumber(d.getPrescription().getPrescriptionNumber())
                .pharmacyUserId(d.getPharmacyUser().getId())
                .pharmacyName(d.getPharmacyUser().getFullName())
                .patientId(d.getPatient().getId())
                .patientName(d.getPatient().getFullName())
                .status(d.getStatus())
                .notes(d.getNotes())
                .rejectionReason(d.getRejectionReason())
                .createdAt(d.getCreatedAt())
                .updatedAt(d.getUpdatedAt())
                .build();
    }
}