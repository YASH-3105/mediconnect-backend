package com.mediconnect.repository;

import com.mediconnect.entity.DispensingRecord;
import com.mediconnect.entity.DispenseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DispensingRecordRepository
        extends JpaRepository<DispensingRecord, Long> {

    Page<DispensingRecord> findByPharmacyUserIdOrderByCreatedAtDesc(
            Long pharmacyUserId, Pageable pageable);

    Page<DispensingRecord> findByPharmacyUserIdAndStatusOrderByCreatedAtDesc(
            Long pharmacyUserId, DispenseStatus status, Pageable pageable);

    Optional<DispensingRecord> findByPrescriptionId(Long prescriptionId);

    List<DispensingRecord> findByPatientId(Long patientId);
}