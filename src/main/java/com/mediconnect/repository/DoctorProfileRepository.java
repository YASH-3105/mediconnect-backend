package com.mediconnect.repository;

import com.mediconnect.entity.DoctorProfile;
import com.mediconnect.entity.Specialization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DoctorProfileRepository extends JpaRepository<DoctorProfile, Long> {

    Optional<DoctorProfile> findByUserId(Long userId);

    boolean existsByUserId(Long userId);

    boolean existsByLicenseNumber(String licenseNumber);
    
    long countByApprovedFalse();

    @Query("SELECT d FROM DoctorProfile d WHERE d.approved = true " +
           "AND (:specialization IS NULL OR d.specialization = :specialization) " +
           "AND (:city IS NULL OR LOWER(d.city) LIKE LOWER(CONCAT('%', :city, '%'))) " +
           "AND (:minFee IS NULL OR d.consultationFee >= :minFee) " +
           "AND (:maxFee IS NULL OR d.consultationFee <= :maxFee) " +
           "AND (:acceptingNewPatients IS NULL OR d.acceptingNewPatients = :acceptingNewPatients)")
    Page<DoctorProfile> searchDoctors(
            @Param("specialization") Specialization specialization,
            @Param("city") String city,
            @Param("minFee") Double minFee,
            @Param("maxFee") Double maxFee,
            @Param("acceptingNewPatients") Boolean acceptingNewPatients,
            Pageable pageable);

    Page<DoctorProfile> findByApprovedTrue(Pageable pageable);
}