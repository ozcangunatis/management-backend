package com.example.management.controller;

import com.example.management.dto.LeaveRequestDto;
import com.example.management.models.Enum.LeaveStatus;
import com.example.management.models.LeaveRequest;
import com.example.management.service.LeaveRequestService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.management.request.UpdateLeaveStatusRequest;
import com.example.management.response.LeaveRequestResponse;
import com.example.management.request.UpdateLeaveStatusRequest;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/leave-requests")
public class LeaveRequestController {
    private final LeaveRequestService leaveRequestService;
    //yeni izin talebi
    @PostMapping("/{userId}")
    public ResponseEntity<?> createLeaveRequest(
            @PathVariable Long userId,
            @RequestBody LeaveRequest leaveRequest
    ) {
        System.out.println("🚀 Leave request geldi! User ID: " + userId);
        LeaveRequest createdRequest = leaveRequestService.createLeaveRequest(userId, leaveRequest);
        return ResponseEntity.ok(createdRequest);

    }
@PutMapping("/status")
    public ResponseEntity<?> updateLeaveStatus(@RequestBody UpdateLeaveStatusRequest request) {
        try {
            LeaveRequestResponse updated = leaveRequestService.updateLeaveRequestStatus(request);
            return ResponseEntity.ok(updated);
        }
        catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
}
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getLeaveRequestsByUserId(@PathVariable Long userId) {
        List<LeaveRequest> leaveRequests = leaveRequestService.getLeaveRequestByUserId(userId);
        return ResponseEntity.ok(leaveRequests);
    }
    @GetMapping
    public ResponseEntity<List<LeaveRequestDto>> getAllLeaveRequests() {
        List<LeaveRequestDto> leaveRequests = leaveRequestService.getAllLeaveRequest();
        return ResponseEntity.ok(leaveRequests);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLeaveRequest(@PathVariable Long id) {
        boolean deleted = leaveRequestService.deleteLeaveRequest(id);
        if (deleted) {
            return ResponseEntity.ok("Leave request deleted successfully.");
        } else {
            return ResponseEntity.status(404).body("Leave request not found.");
        }
    }

}

