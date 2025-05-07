package com.example.management.service;

import com.example.management.dto.UserDto;
import com.example.management.mapper.UserMapper;
import com.example.management.model.Office;
import com.example.management.model.User;
import com.example.management.repositories.OfficeRepository;
import com.example.management.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OfficeService {

    private final OfficeRepository officeRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    public Office createOffice(Office office) {
        return officeRepository.save(office);
    }


    public boolean isOfficeExists(String officeName) {
        return officeRepository.existsByOfficeName(officeName);
    }


    public List<Office> getAllOffices() {
        return officeRepository.findAll();
    }


    public UserDto getUserByIdWithOfficeControl(Long targetUserId, User currentUser) {
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new IllegalArgumentException("Target user not found."));

        if (currentUser.getRole().name().equals("ADMIN") || currentUser.getRole().name().equals("SUPER_HR")) {
            return userMapper.toDto(targetUser);
        }

        if (currentUser.getRole().name().equals("HR")) {
            if (currentUser.getOffice() == null || targetUser.getOffice() == null
                    || !currentUser.getOffice().getId().equals(targetUser.getOffice().getId())) {
                throw new RuntimeException("HR can only see users in their own office");
            }
            return userMapper.toDto(targetUser);
        }

        if (currentUser.getRole().name().equals("EMPLOYEE")) {
            if (!currentUser.getId().equals(targetUser.getId())) {
                throw new RuntimeException("Employees can only see their own information.");
            }
            return userMapper.toDto(targetUser);
        }

        throw new RuntimeException("unauthorized access");
    }


}



