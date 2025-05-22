package com.example.management.repositories;

import com.example.management.model.enums.LeaveStatus;
import com.example.management.model.LeaveRequest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    List<LeaveRequest> findApprovedLeavesByUserId(@Param("userId") Long userId);


    @Query("SELECT lr FROM LeaveRequest lr WHERE lr.user.office.id = :officeId")
    List<LeaveRequest> findByUserOfficeId(@Param("officeId") Long officeId);


    List<LeaveRequest> findByUser_Office_IdAndStartDateBetween(Long officeId, LocalDate start, LocalDate end);
    int countByCreatedAtAfter(LocalDate date);
    @Query(value = """
    SELECT COALESCE(SUM(DATE_PART('day', end_date - start_date) + 1), 0)
    FROM leave_request
    WHERE status = 'APPROVED' 
      AND start_date >= :start 
      AND start_date <= :end
    """, nativeQuery = true)
    int sumApprovedLeaveDaysThisMonth(@Param("start") LocalDate start, @Param("end") LocalDate end);
    @Query(value = """
    SELECT COUNT(DISTINCT user_id)
    FROM leave_request
    WHERE status = 'APPROVED'
      AND start_date >= :start 
      AND start_date <= :end
    """, nativeQuery = true)
    int countDistinctUsersWithApprovedLeavesThisMonth(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @EntityGraph(attributePaths = {"user", "user.office"})
    Optional<LeaveRequest> findWithUserOfficeById(Long id);

}
