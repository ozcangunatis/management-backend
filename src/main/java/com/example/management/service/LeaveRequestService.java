package com.example.management.service;

import com.example.management.dto.StatsResponseDto;
import com.example.management.dto.UpdateLeaveRequestDatesRequest;
import com.example.management.mapper.LeaveRequestMapper;
import com.example.management.models.LeaveType;
import com.example.management.models.enums.LeaveStatus;
import com.example.management.models.LeaveBalance;
import com.example.management.models.LeaveRequest;
import com.example.management.models.User;
import com.example.management.models.enums.LeaveTypeEnum;
import com.example.management.repositories.LeaveBalanceRepository;
import com.example.management.repositories.LeaveRequestRepository;
import com.example.management.repositories.LeaveTypeRepository;
import com.example.management.repositories.UserRepository;
import com.example.management.request.LeaveRequestCreateRequest;
import com.example.management.response.LeaveRequestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.management.request.UpdateLeaveStatusRequest;



import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



@Service
@RequiredArgsConstructor
public class LeaveRequestService {
    private final LeaveTypeRepository leaveTypeRepository;

    private final LeaveRequestRepository leaveRequestRepository;
    private final UserRepository userRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final LeaveBalanceService leaveBalanceService;


    public List<LeaveRequest> getLeaveRequestByUserId(Long userId) {
        return leaveRequestRepository.findByUserId(userId);
    }

    public LeaveRequest createLeaveRequest(Long userId, LeaveRequestCreateRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        boolean hasPending = leaveRequestRepository.existsByUserIdAndStatus(userId, LeaveStatus.PENDING);
        if (hasPending) {
            throw new IllegalArgumentException("User has already pending request.");
        }

        int requestDays = calculateDays(request.getStartDate(), request.getEndDate());
        if (requestDays <= 0) {
            throw new IllegalArgumentException("Invalid date range!");
        }

        com.example.management.models.LeaveType leaveType =
                leaveTypeRepository.findByLeaveType(request.getLeaveType().name())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid leave type"));

        LeaveBalance balance = leaveBalanceRepository
                .findByUserIdAndLeaveTypeId(userId, leaveType.getId())
                .orElseThrow(() -> new IllegalArgumentException("Leave balance not found for user and leave type!"));

        if (balance.getRemainingDays() < requestDays) {
            throw new IllegalArgumentException("Not enough remaining leave days.");
        }

        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setUser(user);
        leaveRequest.setLeaveType(leaveType);
        leaveRequest.setStartDate(request.getStartDate());
        leaveRequest.setEndDate(request.getEndDate());
        leaveRequest.setDescription(request.getDescription());
        leaveRequest.setReports(request.getReports());
        leaveRequest.setStatus(LeaveStatus.PENDING);
        leaveRequest.setCreatedAt(LocalDateTime.now());

        return leaveRequestRepository.save(leaveRequest);
    }

    @Transactional

    public int calculateDays(LocalDate start, LocalDate end) {
        return (int) (end.toEpochDay() - start.toEpochDay()) + 1;
    }
    @Transactional
    public LeaveRequestResponse updateLeaveRequestStatus(UpdateLeaveStatusRequest request) {
        Optional<LeaveRequest> leaveRequestOpt = leaveRequestRepository.findById(request.getRequestId());

        if (leaveRequestOpt.isEmpty()) {
            throw new IllegalArgumentException("Leave request not found!");
        }

        LeaveRequest leaveRequest = leaveRequestOpt.get();
        LeaveStatus newStatus = request.getNewStatus();
        leaveRequest.setStatus(newStatus);

        if (newStatus == LeaveStatus.APPROVED) {
            int requestDays = calculateDays(leaveRequest.getStartDate(), leaveRequest.getEndDate());
            LeaveType leaveType = leaveRequest.getLeaveType();

            LeaveBalance balance = leaveBalanceRepository
                    .findByUserIdAndLeaveTypeId(leaveRequest.getUser().getId(), leaveType.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Leave balance not found for user and type!"));

            if (balance.getRemainingDays() < requestDays) {
                throw new IllegalStateException("Not enough leave days to approve this request.");
            }

            balance.setUsedDays(balance.getUsedDays() + requestDays);
            balance.calculateRemainingDays();
            leaveBalanceRepository.save(balance);
        }

        LeaveRequest updated = leaveRequestRepository.save(leaveRequest);
        return toResponseDto(updated);
    }



    private LeaveRequestResponse toResponseDto(LeaveRequest leaveRequest) {
        LeaveRequestResponse dto = new LeaveRequestResponse();
        dto.setId(leaveRequest.getId());
        dto.setUserId(leaveRequest.getUser().getId());
        dto.setLeaveType(leaveRequest.getLeaveType().getLeaveType());
        dto.setStartDate(leaveRequest.getStartDate());
        dto.setEndDate(leaveRequest.getEndDate());
        dto.setStatus(leaveRequest.getStatus().name());
        dto.setReports(leaveRequest.getReports());
        dto.setDescription(leaveRequest.getDescription());
        return dto;
    }
    private final LeaveRequestMapper leaveRequestMapper;
    public List<LeaveRequestResponse> getAllLeaveRequest() {
        List<LeaveRequest> requests = leaveRequestRepository.findAll();
        return leaveRequestMapper.toDtoList(requests);
    }
    @Transactional
    public boolean deleteLeaveRequest(Long id){
        if (leaveRequestRepository.existsById(id)) {
            leaveRequestRepository.deleteById(id);
            return true;
        }
        return false;
    }
    private User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No authenticated user found.");
        }

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    public List<LeaveRequestResponse> getLeaveRequestsForCurrentUser() {
        User currentUser = getCurrentAuthenticatedUser();
        List<LeaveRequest> leaveRequests = leaveRequestRepository.findByUserId(currentUser.getId());
        if(
                leaveRequests.isEmpty()){
            throw new IllegalStateException("No leave requests found!");
        }
        return leaveRequests.stream()
                .map(leaveRequestMapper::toDto)
                .collect(Collectors.toList());
    }
    public List<LeaveRequestResponse> getAllLeaveRequestsByDateRange(LocalDate start, LocalDate end) {
        List<LeaveRequest> requests = leaveRequestRepository
                .findByStartDateGreaterThanEqualAndEndDateLessThanEqual(start, end);
        if(requests.isEmpty()){
            throw new IllegalStateException("No leave requests found!");
        }
        return requests.stream()
                .map(leaveRequestMapper::toDto)
                .collect(Collectors.toList());
    }
    public List<LeaveRequestResponse> getMyLeaveRequestsByDateRange(LocalDate start, LocalDate end) {
        User currentUser = getCurrentAuthenticatedUser();

        List<LeaveRequest> leaveRequests = leaveRequestRepository.findByUserIdAndStartDateBetween(
                currentUser.getId(), start, end);

        if (leaveRequests.isEmpty()) {
            throw new IllegalStateException("No leave requests were found between the specified dates.");
        }

        return leaveRequests.stream()
                .map(leaveRequestMapper::toDto)
                .collect(Collectors.toList());
    }
    public List<LeaveRequestResponse> getLeaveRequestsByUserIdAndDateRange(Long userId, LocalDate start, LocalDate end) {
        List<LeaveRequest> leaveRequests = leaveRequestRepository.findByUserIdAndStartDateBetween(userId, start, end);

        if (leaveRequests.isEmpty()) {
            throw new IllegalStateException("No leave requests were found for this person between the specified dates.");
        }

        return leaveRequests.stream()
                .map(leaveRequestMapper::toDto)
                .collect(Collectors.toList());
    }

    public LeaveRequest updateLeaveRequestDates(Long requestId,Long currentUserId, UpdateLeaveRequestDatesRequest requestDto) {
        Optional<LeaveRequest> optionalRequestOpt = leaveRequestRepository.findById(requestId);
        if (optionalRequestOpt.isEmpty()) {
            throw new IllegalArgumentException("Leave request not found!");
        }
        LeaveRequest leaveRequest = optionalRequestOpt.get();
        if (leaveRequest.getUser().getId() != currentUserId) {
            throw new SecurityException("You can only update your own leave requests.");
        }
        if (!leaveRequest.getStatus().equals(LeaveStatus.PENDING)) {
            throw new IllegalStateException("Only pending leave requests can be updated.");
        }
        if (requestDto.getStartDate() != null) {
            leaveRequest.setStartDate(requestDto.getStartDate());
        }
        if (requestDto.getEndDate() != null) {
            leaveRequest.setEndDate(requestDto.getEndDate());
        }
        return leaveRequestRepository.save(leaveRequest);
    }
     //===========================================DASHBOARD======================================
    public StatsResponseDto getStats() {
        long totalUsers = userRepository.count();
        long totalLeaveRequests = leaveRequestRepository.count();
        long pendingLeaveRequests = leaveRequestRepository.countByStatus(LeaveStatus.PENDING);
        long approvedLeaveRequests = leaveRequestRepository.countByStatus(LeaveStatus.APPROVED);
        long rejectedLeaveRequests = leaveRequestRepository.countByStatus(LeaveStatus.REJECTED);

        List<Object[]> montlyCounts = leaveRequestRepository.countLeaveRequestsByMonth();

        String mostPopularLeaveMonth= "N/A";
        int maxCount= 0;

        for (Object[] row : montlyCounts) {
            int monthValue = ((Number) row[0]).intValue(); // 1-12
            int count = ((Number) row[1]).intValue();

            if (count > maxCount) {
                maxCount = count;
                mostPopularLeaveMonth = Month.of(monthValue).name();
            }
        }
        return new StatsResponseDto(
                totalUsers,
                totalLeaveRequests,
                pendingLeaveRequests,
                approvedLeaveRequests,
                rejectedLeaveRequests,
                mostPopularLeaveMonth
        );
    }

}