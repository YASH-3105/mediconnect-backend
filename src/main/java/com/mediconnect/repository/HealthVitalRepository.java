package com.mediconnect.repository;

import com.mediconnect.entity.HealthVital;
import com.mediconnect.entity.VitalType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HealthVitalRepository extends JpaRepository<HealthVital, Long> {

    Page<HealthVital> findByPatientIdOrderByRecordedAtDesc(
            Long patientId, Pageable pageable);

    List<HealthVital> findByPatientIdAndVitalTypeAndRecordedAtBetweenOrderByRecordedAtAsc(
            Long patientId, VitalType vitalType,
            LocalDateTime start, LocalDateTime end);

    List<HealthVital> findByPatientIdAndVitalTypeOrderByRecordedAtDesc(
            Long patientId, VitalType vitalType);
}