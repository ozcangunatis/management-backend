package com.example.management.controller;

import com.example.management.dto.LeaveRequestDto;
import com.example.management.dto.UpdateLeaveRequestDatesRequest;
import com.example.management.mapper.LeaveRequestMapper;
import com.example.management.models.Enum.LeaveStatus;
import com.example.management.models.LeaveRequest;
import com.example.management.models.User;
import com.example.management.repositories.UserRepository;
import com.example.management.service.LeaveRequestService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.example.management.request.UpdateLeaveStatusRequest;
import com.example.management.response.LeaveRequestResponse;
import com.example.management.request.UpdateLeaveStatusRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/leave-requests")
public class LeaveRequestController {
    private final LeaveRequestService leaveRequestService;
    private final UserRepository userRepository;
    private final LeaveRequestMapper leaveRequestMapper;


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
    @GetMapping("/my-requests")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<?> getMyLeaveRequests() {
        try {
            List<LeaveRequestDto> leaveRequests = leaveRequestService.getLeaveRequestsForCurrentUser();
            return ResponseEntity.ok(leaveRequests);
        } catch (IllegalStateException e) {
            return ResponseEntity.ok(e.getMessage());
        }
    }
    @GetMapping("/filter-by-date")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<?> filterAllLeaveRequestsByDate(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<LeaveRequestDto> result = leaveRequestService.getAllLeaveRequestsByDateRange(startDate, endDate);
            return ResponseEntity.ok(result);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
    @GetMapping("/my/filter-by-date")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<?> filterMyLeaveRequestsByDate(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        try {
            List<LeaveRequestDto> result = leaveRequestService.getMyLeaveRequestsByDateRange(startDate, endDate);
            return ResponseEntity.ok(result);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
    @GetMapping("/filter-by-date/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<?> filterLeaveRequestsByDateAndUser(
            @PathVariable Long userId,
           @Validated @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Validated @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<LeaveRequestDto> result = leaveRequestService.getLeaveRequestsByUserIdAndDateRange(userId, startDate, endDate);
            return ResponseEntity.ok(result);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<?> updateLeaveRequestRequestDates (

            @PathVariable Long id,
            @RequestBody UpdateLeaveRequestDatesRequest requestDto,
            Authentication authentication) {
        String email = authentication.getName();
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found.");
        }
        Long currentUserId = userOpt.get().getId();

        try {
            LeaveRequest updatedRequest = leaveRequestService.updateLeaveRequestDates(id , currentUserId, requestDto);
            LeaveRequestDto responseDto = leaveRequestMapper.toDto(updatedRequest);

            return ResponseEntity.ok(responseDto);
        } catch (IllegalArgumentException | IllegalStateException | SecurityException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}

