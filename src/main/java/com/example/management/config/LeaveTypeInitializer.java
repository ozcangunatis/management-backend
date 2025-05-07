package com.example.management.config;

import com.example.management.model.enums.LeaveTypeEnum;
import com.example.management.repositories.LeaveTypeRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LeaveTypeInitializer {

    private final LeaveTypeRepository leaveTypeRepository;

    @PostConstruct
    public void initLeaveTypes() {
        for (LeaveTypeEnum type : LeaveTypeEnum.values()) {
            boolean exists = leaveTypeRepository.findByLeaveType(type.name()).isPresent();
            if (!exists) {
                com.example.management.model.LeaveType leaveType = new com.example.management.model.LeaveType();
                leaveType.setLeaveType(type.name());
                leaveTypeRepository.save(leaveType);
                System.out.println("✅ LeaveType eklendi: " + type.name());
            }
        }
    }
}
