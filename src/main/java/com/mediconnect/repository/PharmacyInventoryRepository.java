package com.mediconnect.repository;

import com.mediconnect.entity.PharmacyInventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PharmacyInventoryRepository
        extends JpaRepository<PharmacyInventory, Long> {

    Page<PharmacyInventory> findByPharmacyUserIdAndActiveTrue(
            Long pharmacyUserId, Pageable pageable);

    List<PharmacyInventory> findByPharmacyUserIdAndActiveTrueAndMedicineNameContainingIgnoreCase(
            Long pharmacyUserId, String medicineName);

    @Query("SELECT p FROM PharmacyInventory p WHERE p.pharmacyUser.id = :userId " +
           "AND p.active = true AND p.stockQuantity <= p.reorderThreshold")
    List<PharmacyInventory> findLowStockItems(@Param("userId") Long userId);
}