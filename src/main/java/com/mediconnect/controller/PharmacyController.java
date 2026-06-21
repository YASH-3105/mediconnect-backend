package com.mediconnect.controller;

import com.mediconnect.dto.request.DispenseRequest;
import com.mediconnect.dto.request.PharmacyInventoryRequest;
import com.mediconnect.dto.response.DispensingRecordResponse;
import com.mediconnect.dto.response.PagedResponse;
import com.mediconnect.dto.response.PharmacyInventoryResponse;
import com.mediconnect.service.PharmacyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pharmacy")
@RequiredArgsConstructor
public class PharmacyController {

    private final PharmacyService pharmacyService;

    @PostMapping("/{pharmacyUserId}/inventory")
    public ResponseEntity<PharmacyInventoryResponse> addInventory(
            @PathVariable Long pharmacyUserId,
            @Valid @RequestBody PharmacyInventoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pharmacyService.addInventory(pharmacyUserId, request));
    }

    @PutMapping("/inventory/{inventoryId}")
    public ResponseEntity<PharmacyInventoryResponse> updateInventory(
            @PathVariable Long inventoryId,
            @Valid @RequestBody PharmacyInventoryRequest request) {
        return ResponseEntity.ok(pharmacyService.updateInventory(inventoryId, request));
    }

    @PatchMapping("/inventory/{inventoryId}/stock")
    public ResponseEntity<PharmacyInventoryResponse> updateStock(
            @PathVariable Long inventoryId,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(pharmacyService.updateStock(inventoryId, quantity));
    }

    @DeleteMapping("/inventory/{inventoryId}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Long inventoryId) {
        pharmacyService.deleteInventory(inventoryId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{pharmacyUserId}/inventory")
    public ResponseEntity<PagedResponse<PharmacyInventoryResponse>> getInventory(
            @PathVariable Long pharmacyUserId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
                pharmacyService.getInventory(pharmacyUserId, page, size));
    }

    @GetMapping("/{pharmacyUserId}/inventory/low-stock")
    public ResponseEntity<List<PharmacyInventoryResponse>> getLowStock(
            @PathVariable Long pharmacyUserId) {
        return ResponseEntity.ok(pharmacyService.getLowStockItems(pharmacyUserId));
    }

    @PostMapping("/{pharmacyUserId}/dispense/{prescriptionId}")
    public ResponseEntity<DispensingRecordResponse> dispense(
            @PathVariable Long pharmacyUserId,
            @PathVariable Long prescriptionId,
            @Valid @RequestBody DispenseRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pharmacyService.dispensePrescription(
                        pharmacyUserId, prescriptionId, request));
    }

    @GetMapping("/{pharmacyUserId}/queue")
    public ResponseEntity<PagedResponse<DispensingRecordResponse>> getQueue(
            @PathVariable Long pharmacyUserId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
                pharmacyService.getDispensingQueue(pharmacyUserId, page, size));
    }

    @GetMapping("/dispense/{recordId}")
    public ResponseEntity<DispensingRecordResponse> getRecord(
            @PathVariable Long recordId) {
        return ResponseEntity.ok(pharmacyService.getDispensingRecord(recordId));
    }
}