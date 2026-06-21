package com.mediconnect.repository;

import com.mediconnect.entity.LeaveBlock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveBlockRepository extends JpaRepository<LeaveBlock, Long> {

    List<LeaveBlock> findByDoctorId(Long doctorId);

    boolean existsByDoctorIdAndLeaveDate(Long doctorId, LocalDate leaveDate);

    List<LeaveBlock> findByDoctorIdAndLeaveDateBetween(
            Long doctorId, LocalDate startDate, LocalDate endDate);
}