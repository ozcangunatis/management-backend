package com.example.management.repositories;

import com.example.management.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/*
  USER ENTITY ICIN VERITABANI ISLEMLERI ICIN AÇILAN REPOSITORY SINIFI
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findByRole(com.example.management.models.Enum.Role role);

    @Query("SELECT u FROM User u WHERE u.role IN ('ADMIN', 'HR')")
    List<User> findAdminsAndHr();
}
