package com.example.management.controller;

import com.example.management.dto.UpdateLeaveRequestDatesRequest;
import com.example.management.mapper.LeaveRequestMapper;
import com.example.management.models.LeaveRequest;
import com.example.management.models.User;
import com.example.management.models.enums.LeaveStatus;
import com.example.management.repositories.LeaveRequestRepository;
import com.example.management.repositories.UserRepository;
import com.example.management.request.LeaveRequestCreateRequest;
import com.example.management.service.LeaveRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.example.management.request.UpdateLeaveStatusRequest;
import com.example.management.response.LeaveRequestResponse;




import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/leave-requests")
public class LeaveRequestController {
    private final LeaveRequestService leaveRequestService;
    private final UserRepository userRepository;
    private final LeaveRequestMapper leaveRequestMapper;
    private final LeaveRequestRepository leaveRequestRepository;



    //yeni izin talebi
    @PostMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<?> createLeaveRequest(
            @RequestBody LeaveRequestCreateRequest requestDto,
            Authentication authentication
    )
    {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        System.out.println("🚀 Leave request geldi! Kullanıcı email: " + email);

        LeaveRequest createdRequest = leaveRequestService.createLeaveRequest(user.getId(), requestDto);
        LeaveRequestResponse responseDto = leaveRequestMapper.toDto(createdRequest);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
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
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<?> getLeaveRequestsByUserId(@PathVariable Long userId) {
        List<LeaveRequestResponse> leaveRequests = leaveRequestService.getLeaveRequestByUserId(userId)
                .stream()
                .map(leaveRequestMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(leaveRequests);
    }
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'SUPER_HR')")
    public ResponseEntity<List<LeaveRequestResponse>> getAllLeaveRequests() {
        List<LeaveRequestResponse> leaveRequests = leaveRequestService.getAllLeaveRequest();
        return ResponseEntity.ok(leaveRequests);
    }
    @DeleteMapping("/my")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<?> deleteMyPendingLeaveRequest(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Optional<LeaveRequest> optionalRequest = leaveRequestRepository
                .findFirstByUserIdAndStatusOrderByCreatedAtDesc(user.getId(), LeaveStatus.PENDING);

        if (optionalRequest.isEmpty()) {
            return ResponseEntity.status(404).body("No pending leave request found to delete.");
        }

        leaveRequestRepository.deleteById(optionalRequest.get().getId());
        return ResponseEntity.ok("Your pending leave request has been deleted.");
    }

    @GetMapping("/my-requests")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<?> getMyLeaveRequests() {
        try {
            List<LeaveRequestResponse> leaveRequests = leaveRequestService.getLeaveRequestsForCurrentUser();
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
            List<LeaveRequestResponse> result = leaveRequestService.getAllLeaveRequestsByDateRange(startDate, endDate);
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
            List<LeaveRequestResponse> result = leaveRequestService.getMyLeaveRequestsByDateRange(startDate, endDate);
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
            List<LeaveRequestResponse> result = leaveRequestService.getLeaveRequestsByUserIdAndDateRange(userId, startDate, endDate);
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
            LeaveRequestResponse responseDto = leaveRequestMapper.toDto(updatedRequest);

            return ResponseEntity.ok(responseDto);
        } catch (IllegalArgumentException | IllegalStateException | SecurityException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}

