package com.example.management.config;

import com.example.management.model.LeaveBalance;
import com.example.management.model.LeaveType;
import com.example.management.model.User;
import com.example.management.repositories.LeaveBalanceRepository;
import com.example.management.repositories.LeaveTypeRepository;
import com.example.management.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class LeaveBalanceInitializer {

    private final UserRepository userRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;

    @PostConstruct
    public void initLeaveBalances() {
        int currentYear = LocalDate.now().getYear();

        for (User user : userRepository.findAll()) {
            for (LeaveType leaveType : leaveTypeRepository.findAll()) {

                boolean exists = leaveBalanceRepository
                        .findByUserIdAndLeaveTypeId(user.getId(), leaveType.getId())
                        .isPresent();

                if (!exists) {
                    LeaveBalance balance = new LeaveBalance();
                    balance.setUser(user);
                    balance.setLeaveType(leaveType);
                    balance.setLeaveYear(currentYear);
                    balance.setAnnualLeaveDays(14);
                    balance.setUsedDays(0);
                    balance.calculateRemainingDays();

                    leaveBalanceRepository.save(balance);
                    System.out.println("user=" + user.getEmail() + ", type=" + leaveType.getLeaveType());
                }
            }
        }
    }
}
