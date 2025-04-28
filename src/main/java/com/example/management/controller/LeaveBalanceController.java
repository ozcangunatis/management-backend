package com.example.management.controller;

import com.example.management.dto.LeaveBalanceDto;
import com.example.management.dto.UpdateLeaveBalanceRequestDto;
import com.example.management.mapper.LeaveBalanceMapper;
import com.example.management.models.LeaveBalance;
import com.example.management.service.LeaveBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/leave-balance")
@RequiredArgsConstructor
public class LeaveBalanceController {

    private final LeaveBalanceService leaveBalanceService;
    private final LeaveBalanceMapper leaveBalanceMapper;


    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'SUPER_HR')")
    public ResponseEntity<?> getBalanceByUserId(@PathVariable Long userId) {

        Optional<LeaveBalance> balance = leaveBalanceService.getLeaveBalanceByUserId(userId);

        if (balance.isEmpty()) {

            return ResponseEntity.badRequest()
                    .body("Leave balance not found for userId: " + userId);
        }


        return ResponseEntity.ok(balance.get());
    }
    @PostMapping("/{userId}/{totalDays}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<LeaveBalanceDto> createLeaveBalance(@PathVariable Long userId, @PathVariable int totalDays) {
        try {
            LeaveBalance newBalance = leaveBalanceService.createLeaveBalance(userId, totalDays);
            LeaveBalanceDto dto = leaveBalanceMapper.toDto(newBalance);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateLeaveBalance(@PathVariable Long userId,
                                                @RequestBody UpdateLeaveBalanceRequestDto requestDto) {
        boolean updated = leaveBalanceService.updateLeaveBalance(userId, requestDto);
        if (updated) {
            return ResponseEntity.ok("Leave balance updated");
        }else{
            return ResponseEntity.status(400).body("Leave balance not found user.");
        }
    }
    @GetMapping("/my-balance")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<?> getMyLeaveBalance() {
        try {
            LeaveBalanceDto dto = leaveBalanceService.getMyLeaveBalance();
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }

}
