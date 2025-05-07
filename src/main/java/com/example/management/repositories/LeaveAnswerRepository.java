package com.example.management.repositories;

import com.example.management.model.LeaveAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveAnswerRepository extends JpaRepository<LeaveAnswer, Long> {
}
