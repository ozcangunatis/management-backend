package com.example.management.repositories;

import com.example.management.model.Office;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfficeRepository extends JpaRepository<Office, Long> {

    boolean existsByOfficeName(String officeName);
}
