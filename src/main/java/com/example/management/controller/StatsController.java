package com.example.management.controller;

import com.example.management.dto.DashboardStatsDTO;
import com.example.management.service.LeaveStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

    private final LeaveStatsService statsService;

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardStatsDTO> getDashboardStats() {
        return ResponseEntity.ok(statsService.getDashboardStats());
    }
}
