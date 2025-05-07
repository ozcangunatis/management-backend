package com.example.management.mapper;

import com.example.management.dto.LeaveBalanceDto;
import com.example.management.model.LeaveBalance;
import org.springframework.stereotype.Component;

@Component
public class LeaveBalanceMapper {

    public LeaveBalanceDto toDto(LeaveBalance entity) {
        LeaveBalanceDto dto = new LeaveBalanceDto();
        dto.setUserId(entity.getUser().getId());
        dto.setTotalDays(entity.getAnnualLeaveDays());
        dto.setUsedDays(entity.getUsedDays());
        dto.setRemainingDays(entity.getRemainingDays());
        return dto;
    }
}
