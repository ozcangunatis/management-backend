package com.example.management.controller;

import com.example.management.model.Office;
import com.example.management.service.OfficeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offices")
@RequiredArgsConstructor
public class OfficeController {

    private final OfficeService officeService;


    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Office> createOffice(@RequestBody Office office) {
        if (officeService.isOfficeExists(office.getOfficeName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // 409 Conflict
        }
        return ResponseEntity.ok(officeService.createOffice(office));
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_HR')")
    public ResponseEntity<List<Office>> getAllOffices() {
        return ResponseEntity.ok(officeService.getAllOffices());
    }
}