package com.example.management.repositories;

import com.example.management.models.enums.LeaveStatus;
import com.example.management.models.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    Optional<LeaveRequest> findFirstByUserIdAndStatusOrderByCreatedAtDesc(Long userId, LeaveStatus status);


    long countByStatus(LeaveStatus status);

    @Query("SELECT MONTH(l.startDate), COUNT(l) FROM LeaveRequest l GROUP BY MONTH(l.startDate)")
    List<Object[]> countLeaveRequestsByMonth();


    List<LeaveRequest> findByUserId(Long userId);
    List<LeaveRequest> findByStatus(String status);
    boolean existsByIdAndStatus(Long id, String status);
    Optional<LeaveRequest> findByIdAndUserId(Long id, Long userId);
    List<LeaveRequest> findByStartDateGreaterThanEqualAndEndDateLessThanEqual(LocalDate startDate, LocalDate endDate);
    List<LeaveRequest> findByUserIdAndStartDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
    boolean existsByUserIdAndStatus(Long userId, LeaveStatus status);
    @Query("SELECT lr FROM LeaveRequest lr WHERE lr.user.id = :userId AND lr.status = 'APPROVED'")
    List<LeaveRequest> findApprovedLeavesByUserId(Long userId);

}

