package com.mediconnect.repository;

import com.mediconnect.entity.MedicalRecord;
import com.mediconnect.entity.RecordCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

    Page<MedicalRecord> findByPatientIdAndDeletedFalse(Long patientId, Pageable pageable);

    Page<MedicalRecord> findByPatientIdAndCategoryAndDeletedFalse(
            Long patientId, RecordCategory category, Pageable pageable);

    Optional<MedicalRecord> findByShareToken(String shareToken);

    List<MedicalRecord> findByPatientIdAndDeletedTrue(Long patientId);
}