package com.example.management.controller;

import com.example.management.dto.LeaveBalanceDto;
import com.example.management.service.LeaveBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leave-balance")
@RequiredArgsConstructor
public class LeaveBalanceController {

    private final LeaveBalanceService leaveBalanceService;

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'SUPER_HR')")
    public ResponseEntity<?> getBalanceByUserId(@PathVariable Long userId) {
        LeaveBalanceDto dto = leaveBalanceService.getUserLeaveBalance(userId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/my-balance")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'HR', 'SUPER_HR')")
    public ResponseEntity<?> getMyLeaveBalance() {
        LeaveBalanceDto dto = leaveBalanceService.getMyLeaveBalance();
        return ResponseEntity.ok(dto);
    }


}

