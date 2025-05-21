package com.example.management.repositories;

import com.example.management.model.LeaveBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {
    Optional<LeaveBalance> findTopByUserIdOrderByLeaveYearDesc(Long userId);

    Optional<LeaveBalance> findByUserIdAndLeaveTypeId(Long userId, Long leaveTypeId);




    List<LeaveBalance> findByUserId(Long userId);

    boolean existsByUserIdAndRemainingDaysGreaterThanEqual(Long userId, Integer remainingDays);
}

