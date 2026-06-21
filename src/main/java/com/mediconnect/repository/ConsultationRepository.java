package com.mediconnect.repository;

import com.mediconnect.entity.Consultation;
import com.mediconnect.entity.ConsultationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation, Long> {

    Optional<Consultation> findByAppointmentId(Long appointmentId);

    @Query("SELECT c FROM Consultation c WHERE c.appointment.patient.id = :patientId")
    Page<Consultation> findByPatientId(
            @Param("patientId") Long patientId, Pageable pageable);

    @Query("SELECT c FROM Consultation c WHERE c.appointment.doctor.id = :doctorId")
    Page<Consultation> findByDoctorId(
            @Param("doctorId") Long doctorId, Pageable pageable);

    @Query("SELECT c FROM Consultation c WHERE c.appointment.doctor.id = :doctorId " +
           "AND c.status = :status")
    Page<Consultation> findByDoctorIdAndStatus(
            @Param("doctorId") Long doctorId,
            @Param("status") ConsultationStatus status,
            Pageable pageable);
}