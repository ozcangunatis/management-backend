package com.example.management.dto;

import com.example.management.model.enums.LeaveTypeEnum;
import lombok.Data;

@Data
public class GrantLeaveRequestDTO {
    private Long userId;
    private LeaveTypeEnum leaveTypeEnum;
    private int days;
}
