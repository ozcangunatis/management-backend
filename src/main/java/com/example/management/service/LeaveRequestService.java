package com.example.management.service;

import com.example.management.dto.DashboardStatsDTO;
import com.example.management.dto.UpdateLeaveRequestDatesRequest;
import com.example.management.mapper.LeaveRequestMapper;
import com.example.management.model.LeaveType;
import com.example.management.model.enums.LeaveStatus;
import com.example.management.model.LeaveBalance;
import com.example.management.model.LeaveRequest;
import com.example.management.model.User;
import com.example.management.model.enums.LeaveTypeEnum;
import com.example.management.model.enums.Role;
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



import java.nio.file.AccessDeniedException;
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
    private final LeaveRequestMapper leaveRequestMapper;




    public List<LeaveRequest> getLeaveRequestByUserId(Long userId) {
        return leaveRequestRepository.findByUserId(userId);
    }
    public LeaveRequest createLeaveRequest(Long userId, LeaveRequestCreateRequest request) {
        User currentUser = getCurrentAuthenticatedUser();
        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));


        if (currentUser.getRole() == Role.ADMIN || currentUser.getRole() == Role.SUPER_HR) {

        } else if (currentUser.getRole() == Role.HR) {
            if (currentUser.getOffice() == null || targetUser.getOffice() == null ||
                    !currentUser.getOffice().getId().equals(targetUser.getOffice().getId())) {
                throw new SecurityException("You can only create leave requests for users in your office.");
            }
        } else if (currentUser.getRole() == Role.EMPLOYEE) {
            if (!currentUser.getId().equals(targetUser.getId())) {
                throw new SecurityException("You can only create leave requests for yourself.");
            }
        } else {
            throw new SecurityException("Unauthorized operation.");
        }


        boolean hasPending = leaveRequestRepository.existsByUserIdAndStatus(userId, LeaveStatus.PENDING);
        if (hasPending) {
            throw new IllegalArgumentException("User has already pending request.");
        }


        int requestDays = calculateDays(request.getStartDate(), request.getEndDate());
        if (requestDays <= 0) {
            throw new IllegalArgumentException("Invalid date range!");
        }


        LeaveType leaveType = leaveTypeRepository.findByLeaveType(request.getLeaveTypeEnum().name())
                .orElseThrow(() -> new IllegalArgumentException("Invalid leave type"));


        LeaveBalance balance = leaveBalanceRepository
                .findByUserIdAndLeaveTypeId(userId, leaveType.getId())
                .orElseThrow(() -> new IllegalArgumentException("Leave balance not found for user and leave type!"));


        if ("PAID".equals(leaveType.getLeaveType())) {
            if (balance.getRemainingDays() < requestDays) {

                int newUsedDays = balance.getUsedDays() + requestDays;


                if (balance.getUsedDays() == 0 && newUsedDays < 0) {

                    balance.setUsedDays(newUsedDays);
                } else {

                    balance.setUsedDays(balance.getUsedDays() + requestDays);
                }

                balance.calculateRemainingDays();
                leaveBalanceRepository.save(balance);
            } else {
                balance.setUsedDays(balance.getUsedDays() + requestDays);
                balance.calculateRemainingDays();
                leaveBalanceRepository.save(balance);
            }
        }




        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setUser(targetUser);
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
        User currentUser = getCurrentAuthenticatedUser();

        LeaveRequest leaveRequest = leaveRequestRepository.findWithUserOfficeById(request.getRequestId())
                .orElseThrow(() -> new IllegalArgumentException("Leave request not found!"));


        if (currentUser.getRole() == Role.ADMIN || currentUser.getRole() == Role.SUPER_HR) {

        }
        else if (currentUser.getRole() == Role.HR) {
            if (currentUser.getOffice() == null || leaveRequest.getUser().getOffice() == null ||
                    !currentUser.getOffice().getId().equals(leaveRequest.getUser().getOffice().getId())) {
                throw new SecurityException("You can only update leave requests for your office.");
            }
        }
        else {
            throw new SecurityException("You are not authorized to update leave request statuses.");
        }


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

    public List<LeaveRequestResponse> getAllLeaveRequest() {
        User currentUser = getCurrentAuthenticatedUser();

        List<LeaveRequest> requests;

        switch (currentUser.getRole()) {
            case ADMIN, SUPER_HR -> {

                requests = leaveRequestRepository.findAll();
            }
            case HR -> {

                Long officeId = currentUser.getOffice().getId();
                requests = leaveRequestRepository.findByUserOfficeId(officeId);
            }
            default -> throw new SecurityException("You are not authorized to view leave requests.");
        }

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
        User currentUser = getCurrentAuthenticatedUser();
        List<LeaveRequest> requests;

        if (currentUser.getRole() == Role.ADMIN || currentUser.getRole() == Role.SUPER_HR) {
            requests = leaveRequestRepository.findByStartDateGreaterThanEqualAndEndDateLessThanEqual(start, end);
        } else if (currentUser.getRole() == Role.HR) {
            requests = leaveRequestRepository.findByUser_Office_IdAndStartDateBetween(
                    currentUser.getOffice().getId(), start, end);
        } else {
            throw new SecurityException("You are not authorized to view leave requests.");
        }

        if (requests.isEmpty()) {
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

    public void grantLeaveToUser(Long userId, LeaveTypeEnum leaveTypeEnum, int days) {
        User currentUser = getCurrentAuthenticatedUser();
        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));


        if (currentUser.getRole() == Role.HR) {
            if (currentUser.getOffice() == null || targetUser.getOffice() == null ||
                    !currentUser.getOffice().getId().equals(targetUser.getOffice().getId())) {
                throw new SecurityException("HR can only assign leave to users in their office.");

            }
        }

        if (!(currentUser.getRole() == Role.SUPER_HR || currentUser.getRole() == Role.ADMIN || currentUser.getRole() == Role.HR)) {
            throw new SecurityException("Only HR, SUPER_HR, or ADMIN can assign leave.");
        }

        LeaveType leaveType = leaveTypeRepository
                .findByLeaveType(leaveTypeEnum.name())
                .orElseThrow(() -> new IllegalArgumentException("LeaveType not found!"));

        LeaveBalance balance = leaveBalanceRepository
                .findByUserIdAndLeaveTypeId(userId, leaveType.getId())
                .orElseThrow(() -> new IllegalArgumentException("Leave balance not found for user and leave type!"));

        boolean isPaid = "PAID".equals(leaveType.getLeaveType());

        if (isPaid) {
            if (balance.getRemainingDays() < days) {
                throw new IllegalArgumentException("Not enough remaining leave days.");
            }
            balance.setUsedDays(balance.getUsedDays() + days);
            balance.calculateRemainingDays();
            leaveBalanceRepository.save(balance);
        }

        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setUser(targetUser);
        leaveRequest.setLeaveType(leaveType);
        leaveRequest.setStartDate(LocalDate.now());
        leaveRequest.setEndDate(LocalDate.now().plusDays(days));
        leaveRequest.setStatus(LeaveStatus.APPROVED);
        leaveRequest.setCreatedAt(LocalDateTime.now());

        leaveRequestRepository.save(leaveRequest);
    }



}