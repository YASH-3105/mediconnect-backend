package com.mediconnect.repository;

import com.mediconnect.entity.Delegation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DelegationRepository extends JpaRepository<Delegation, Long> {

    List<Delegation> findByDoctorIdAndActiveTrue(Long doctorId);

    Optional<Delegation> findByDoctorIdAndActiveTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Long doctorId, LocalDate start, LocalDate end);
}