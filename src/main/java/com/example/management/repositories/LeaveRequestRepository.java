package com.example.management.repositories;

import com.example.management.models.Enum.LeaveStatus;
import com.example.management.models.LeaveRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByUserId(Long userId);
    //Onaylanmamış izin taleplerini getir
    List<LeaveRequest>findByStatus(String status);
    //belirli kullanıcıya ait belirli bir izin talebi var mı kontrol et
    boolean existsByIdAndStatus(Long id, String status);
    //Belirli bir izin talebini ID ile getirme
    Optional<LeaveRequest> findByIdAndUserId(Long id, Long userId);


}
