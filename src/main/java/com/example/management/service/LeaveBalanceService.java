package com.example.management.service;

import com.example.management.models.LeaveBalance;
import com.example.management.models.User;
import com.example.management.repositories.LeaveBalanceRepository;
import com.example.management.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LeaveBalanceService {

    private final LeaveBalanceRepository leaveBalanceRepository;
    private final UserRepository userRepository;

    /**
     * Kullanıcının izin bakiyesini getirir.
     */
    public Optional<LeaveBalance> getLeaveBalanceByUserId(Long userId) {
        return leaveBalanceRepository.findByUserId(userId);
    }

    /**
     * Yeni bir LeaveBalance kaydı oluşturur.
     */
    public LeaveBalance createLeaveBalance(Long userId, int totalDays) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            LeaveBalance leaveBalance = new LeaveBalance();
            leaveBalance.setUser(userOptional.get());
            leaveBalance.setTotalDays(totalDays);
            leaveBalance.setUsedDays(0); // İlk başta kullanılan gün yok
            leaveBalance.calculateRemainingDays(); // Otomatik hesaplama
            return leaveBalanceRepository.save(leaveBalance);
        } else {
            throw new IllegalArgumentException("User not found!");
        }
    }

    /**
     * Kullanıcının izin günlerini günceller ve kalan günleri hesaplar.
     */
    public LeaveBalance updateUsedDays(Long userId, int usedDays) {
        Optional<LeaveBalance> leaveBalanceOptional = leaveBalanceRepository.findByUserId(userId);

        if (leaveBalanceOptional.isPresent()) {
            LeaveBalance leaveBalance = leaveBalanceOptional.get();

            // Kullanılan izin günlerini güncelle
            int newUsedDays = leaveBalance.getUsedDays() + usedDays;
            if (newUsedDays > leaveBalance.getTotalDays()) {
                throw new IllegalArgumentException("Total days exceeded!");
            }

            leaveBalance.setUsedDays(newUsedDays);
            leaveBalance.calculateRemainingDays(); // Kalan izinleri hesaplat

            return leaveBalanceRepository.save(leaveBalance);
        } else {
            throw new IllegalArgumentException("Leave balance not found!");
        }
    }

    public LeaveBalance saveLeaveBalance(LeaveBalance leaveBalance) {
        return leaveBalanceRepository.save(leaveBalance);
    }
    @Transactional
    public boolean updateLeaveBalance(Long userId, int remainingDays) {
        Optional<LeaveBalance> balanceOpt = leaveBalanceRepository.findByUserId(userId);

        if (balanceOpt.isPresent()) {
            LeaveBalance balance = balanceOpt.get();
            balance.setRemainingDays(remainingDays);
            leaveBalanceRepository.save(balance);
            return true;
        }

        return false;
    }

}