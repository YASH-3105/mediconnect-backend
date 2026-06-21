package com.mediconnect.repository;

import com.mediconnect.entity.Payment;
import com.mediconnect.entity.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByAppointmentId(Long appointmentId);

    Optional<Payment> findByTransactionId(String transactionId);

    Page<Payment> findByPatientId(Long patientId, Pageable pageable);

    Page<Payment> findByPatientIdAndStatus(
            Long patientId, PaymentStatus status, Pageable pageable);
}