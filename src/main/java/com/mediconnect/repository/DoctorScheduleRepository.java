package com.mediconnect.repository;

import com.mediconnect.entity.Day;
import com.mediconnect.entity.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {

    List<DoctorSchedule> findByDoctorIdAndActiveTrue(Long doctorId);

    Optional<DoctorSchedule> findByDoctorIdAndDayOfWeek(Long doctorId, Day dayOfWeek);

    boolean existsByDoctorIdAndDayOfWeek(Long doctorId, Day dayOfWeek);
}