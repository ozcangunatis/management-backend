package com.example.management.controller;

import com.example.management.dto.UpdateLeaveBalanceRequestDto;
import com.example.management.models.LeaveBalance;
import com.example.management.service.LeaveBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/leave-balance")
@RequiredArgsConstructor
public class LeaveBalanceController {

    private final LeaveBalanceService leaveBalanceService;


    @GetMapping("/{userId}")
    public ResponseEntity<?> getBalanceByUserId(@PathVariable Long userId) {

        Optional<LeaveBalance> balance = leaveBalanceService.getLeaveBalanceByUserId(userId);

        if (balance.isEmpty()) {

            return ResponseEntity.badRequest()
                    .body("Leave balance not found for userId: " + userId);
        }


        return ResponseEntity.ok(balance.get());
    }
    @PostMapping("/{userId}/{totalDays}")
    public ResponseEntity<String> createLeaveBalance(@PathVariable Long userId, @PathVariable int totalDays) {
        try {
            LeaveBalance newBalance = leaveBalanceService.createLeaveBalance(userId, totalDays);
            return ResponseEntity.ok().body(newBalance.toString());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateLeaveBalance(@PathVariable Long userId,
                                                @RequestBody UpdateLeaveBalanceRequestDto requestDto) {
        boolean updated = leaveBalanceService.updateLeaveBalance(userId, requestDto.getRemainingDays());
        if (updated) {
            return ResponseEntity.ok("Leave balance updated");
        }else{
            return ResponseEntity.status(400).body("Leave balance not found user.");
        }
    }

}
