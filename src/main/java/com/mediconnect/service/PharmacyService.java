package com.mediconnect.service;

import com.mediconnect.dto.request.DispenseRequest;
import com.mediconnect.dto.request.PharmacyInventoryRequest;
import com.mediconnect.dto.response.DispensingRecordResponse;
import com.mediconnect.dto.response.PagedResponse;
import com.mediconnect.dto.response.PharmacyInventoryResponse;
import java.util.List;

public interface PharmacyService {
    PharmacyInventoryResponse addInventory(Long pharmacyUserId,
                                            PharmacyInventoryRequest request);
    PharmacyInventoryResponse updateInventory(Long inventoryId,
                                               PharmacyInventoryRequest request);
    PharmacyInventoryResponse updateStock(Long inventoryId, Integer quantity);
    void deleteInventory(Long inventoryId);
    PagedResponse<PharmacyInventoryResponse> getInventory(Long pharmacyUserId,
                                                           int page, int size);
    List<PharmacyInventoryResponse> getLowStockItems(Long pharmacyUserId);
    DispensingRecordResponse dispensePrescription(Long pharmacyUserId,
                                                   Long prescriptionId,
                                                   DispenseRequest request);
    PagedResponse<DispensingRecordResponse> getDispensingQueue(Long pharmacyUserId,
                                                                int page, int size);
    DispensingRecordResponse getDispensingRecord(Long recordId);
}