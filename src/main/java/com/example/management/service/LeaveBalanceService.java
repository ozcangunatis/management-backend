package com.example.management.service;

import com.example.management.dto.LeaveBalanceDto;
import com.example.management.dto.UpdateLeaveBalanceRequestDto;
import com.example.management.models.LeaveBalance;
import com.example.management.models.LeaveRequest;
import com.example.management.models.User;
import com.example.management.mapper.LeaveBalanceMapper;
import com.example.management.models.enums.Role;
import com.example.management.repositories.LeaveBalanceRepository;
import com.example.management.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LeaveBalanceService {

    private final LeaveBalanceRepository leaveBalanceRepository;
    private final UserRepository userRepository;
    private final LeaveBalanceMapper leaveBalanceMapper;


    public Optional<LeaveBalance> getLeaveBalanceByUserId(Long userId) {
        return leaveBalanceRepository.findByUserId(userId);
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


    public LeaveBalanceDto getMyLeaveBalance() {
        User user = getCurrentAuthenticatedUser();
        return getUserLeaveBalance(user.getId());
    }


    public int calculateAnnualLeaveDays(LocalDate jobEntryDate) {
        if (jobEntryDate == null) {
            throw new IllegalStateException("Job entry date is null!");
        }

        long yearsWorked = ChronoUnit.YEARS.between(jobEntryDate, LocalDate.now());

        if (yearsWorked < 1) return 0;
        else if (yearsWorked < 5) return 14;
        else if (yearsWorked < 15) return 20;
        else return 26;
    }


    public LeaveBalanceDto getUserLeaveBalance(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        if (user.getRole() == Role.ADMIN) {
            throw new IllegalArgumentException("Admins do not have annual leave rights.");
        }

        int currentYear = LocalDate.now().getYear();


        LeaveBalance balance = leaveBalanceRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    int annualLeave = calculateAnnualLeaveDays(user.getJobEntryDate());
                    LeaveBalance newBalance = new LeaveBalance();
                    newBalance.setUser(user);
                    newBalance.setAnnualLeaveDays(annualLeave);
                    newBalance.setUsedDays(0);
                    newBalance.setLeaveYear(currentYear);
                    newBalance.calculateRemainingDays();
                    return leaveBalanceRepository.save(newBalance);
                });

        int carriedOver = leaveBalanceRepository.findTopByUserIdOrderByLeaveYearDesc(user.getId())
                .map(LeaveBalance::getRemainingDays)
                .orElse(0);

        int total = balance.getAnnualLeaveDays() + carriedOver;
        int usedDays = balance.getUsedDays();
        int remaining = total - usedDays;

        return new LeaveBalanceDto(
                user.getId(),
                total,
                usedDays,
                remaining,
                balance.getAnnualLeaveDays(),
                carriedOver,
                total
        );
    }
    @Transactional
    public void incrementUsedDays(Long userId, int addedDays) {
        LeaveBalance balance = leaveBalanceRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("LeaveBalance not found"));

        int newUsed = balance.getUsedDays() + addedDays;

        if (newUsed > balance.getAnnualLeaveDays()) {
            throw new IllegalArgumentException("Exceeded annual leave limit!");
        }

        balance.setUsedDays(newUsed);
        balance.calculateRemainingDays();

        leaveBalanceRepository.save(balance);
    }






}