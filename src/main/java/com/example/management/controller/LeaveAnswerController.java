package com.example.management.controller;

import com.example.management.response.LeaveAnswerResponse;
import com.example.management.service.LeaveAnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/leave-answers")
@RequiredArgsConstructor
public class LeaveAnswerController {

    private final LeaveAnswerService leaveAnswerService;

    @PostMapping("/approve/{id}")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN', 'SUPER_HR')")
    public ResponseEntity<LeaveAnswerResponse> approveLeaveRequest(
            @PathVariable Long id,
            Authentication authentication
    ) {
        String approverEmail = authentication.getName();
        LeaveAnswerResponse response = leaveAnswerService.approveLeaveRequest(id, approverEmail);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reject/{id}")
    @PreAuthorize("hasAnyRole('HR', 'ADMIN', 'SUPER_HR')")
    public ResponseEntity<LeaveAnswerResponse> rejectLeaveRequest(@PathVariable Long id,
            Authentication authentication
    ) {
        String approverEmail = authentication.getName();
        LeaveAnswerResponse response = leaveAnswerService.rejectLeaveRequest(id, approverEmail);
        return ResponseEntity.ok(response);
    }
}
