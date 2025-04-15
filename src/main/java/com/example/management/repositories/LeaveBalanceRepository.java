package com.example.management.repositories;

import com.example.management.models.LeaveBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {
    Optional<LeaveBalance> findByUserId(Long userId);
    boolean existsByUserIdAndRemainingDaysGreaterThanEqual(Long userId, Integer remainingDays);
}
