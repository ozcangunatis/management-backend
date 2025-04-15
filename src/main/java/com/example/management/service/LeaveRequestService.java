package com.example.management.service;

import com.example.management.mapper.LeaveRequestMapper;
import com.example.management.models.Enum.LeaveStatus;
import com.example.management.models.LeaveBalance;
import com.example.management.models.LeaveRequest;
import com.example.management.models.User;
import com.example.management.repositories.LeaveBalanceRepository;
import com.example.management.repositories.LeaveRequestRepository;
import com.example.management.repositories.UserRepository;
import com.example.management.response.LeaveRequestResponse;
import lombok.RequiredArgsConstructor;
import com.example.management.dto.LeaveRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.management.request.UpdateLeaveStatusRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LeaveRequestService {

    private final LeaveRequestRepository leaveRequestRepository;
    private final UserRepository userRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;

    //Belirli kullancıya ait tüm izin taleplerini getir
    public List<LeaveRequest> getLeaveRequestByUserId(Long userId) {
        return leaveRequestRepository.findByUserId(userId);
    }

    //Yeni izin talebi oluşturma
    @Transactional
    public LeaveRequest createLeaveRequest(Long userId, LeaveRequest leaveRequest) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found!");
        }
        //izin süresi hesaplama
        User user = userOpt.get();
        int requestDays = (int) (leaveRequest.getEndDate().toEpochDay() - leaveRequest.getStartDate().toEpochDay()) + 1;
        if (requestDays < 0) {
            throw new IllegalArgumentException("Invalid date range!");
        }
        //İzin bakiyesi kontrol
        Optional<LeaveBalance> balanceOpt = leaveBalanceRepository.findByUserId(userId);
        if (balanceOpt.isEmpty()) {
            throw new IllegalArgumentException("Leave balance not found!");
        }

        LeaveBalance balance = balanceOpt.get();
        if (balance.getRemainingDays() < requestDays) {
            throw new IllegalArgumentException("Remaining days not enough!");
        }

        leaveRequest.setUser(user);
        leaveRequest.setStatus(LeaveStatus.PENDING);
        leaveRequest.setCreatedAt(LocalDateTime.now());
        leaveRequestRepository.save(leaveRequest);
        balance.setUsedDays(balance.getUsedDays() + requestDays);
        balance.calculateRemainingDays();
        leaveBalanceRepository.save(balance);
        return leaveRequest;

    }

    private int calculateDays(LocalDate start, LocalDate end) {
        return (int) (end.toEpochDay() - start.toEpochDay()) + 1;
    }

    public LeaveRequestResponse updateLeaveRequestStatus(UpdateLeaveStatusRequest request) {
        Optional<LeaveRequest> leaveRequestOpt = leaveRequestRepository.findById(request.getRequestId());

        if (leaveRequestOpt.isEmpty()) {
            throw new IllegalArgumentException("Leave request not found!");
        }

        LeaveRequest leaveRequest = leaveRequestOpt.get();
        leaveRequest.setStatus(request.getNewStatus());
        LeaveRequest updated = leaveRequestRepository.save(leaveRequest);

        // Entity'den Response'a dönüştür
        return toResponseDto(updated);
    }

    private LeaveRequestResponse toResponseDto(LeaveRequest leaveRequest) {
        LeaveRequestResponse dto = new LeaveRequestResponse();
        dto.setId(leaveRequest.getId());
        dto.setUserId(leaveRequest.getUser().getId());
        dto.setLeaveType(leaveRequest.getLeaveType());
        dto.setStartDate(leaveRequest.getStartDate());
        dto.setEndDate(leaveRequest.getEndDate());
        dto.setLeaveStatus(leaveRequest.getStatus());
        return dto;
    }
    private final LeaveRequestMapper leaveRequestMapper;
    public List<LeaveRequestDto> getAllLeaveRequest() {
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

}