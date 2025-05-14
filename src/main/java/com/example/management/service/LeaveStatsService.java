package com.example.management.service;

import com.example.management.dto.DashboardStatsDTO;
import com.example.management.model.enums.LeaveStatus;
import com.example.management.repositories.LeaveRequestRepository;
import com.example.management.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
@Service
@RequiredArgsConstructor
public class LeaveStatsService {
    private final LeaveRequestRepository leaveRequestRepository;
    private final UserRepository userRepository;

    public DashboardStatsDTO getDashboardStats() {
        LocalDate now = LocalDate.now();
        LocalDate sevenDaysAgo = now.minusDays(7);
        LocalDate firstDayOfMonth = now.withDayOfMonth(1);

        int newLeaveRequests = leaveRequestRepository.countByCreatedAtAfter(sevenDaysAgo);
        int pendingLeaveRequests = (int) leaveRequestRepository.countByStatus(LeaveStatus.PENDING);
        int leaveDaysThisMonth = leaveRequestRepository.sumApprovedLeaveDaysThisMonth(firstDayOfMonth, now);
        int employeesOnLeaveThisMonth = leaveRequestRepository.countDistinctUsersWithApprovedLeavesThisMonth(firstDayOfMonth, now);

        return new DashboardStatsDTO(
                newLeaveRequests,
                pendingLeaveRequests,
                leaveDaysThisMonth,
                employeesOnLeaveThisMonth
        );
    }

}
