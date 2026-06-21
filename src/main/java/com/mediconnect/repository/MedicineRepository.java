package com.mediconnect.repository;

import com.mediconnect.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    List<Medicine> findByNameContainingIgnoreCaseAndActiveTrue(String name);

    List<Medicine> findByGenericNameContainingIgnoreCaseAndActiveTrue(String genericName);
}