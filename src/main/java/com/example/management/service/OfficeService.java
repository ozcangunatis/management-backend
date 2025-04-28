package com.example.management.service;

import com.example.management.models.Office;
import com.example.management.repositories.OfficeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OfficeService {

    private final OfficeRepository officeRepository;


    public Office createOffice(Office office) {
        return officeRepository.save(office);
    }


    public boolean isOfficeExists(String officeName) {
        return officeRepository.existsByOfficeName(officeName);
    }


    public List<Office> getAllOffices() {
        return officeRepository.findAll();
    }
}
