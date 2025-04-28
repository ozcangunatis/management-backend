package com.example.management.request;

import com.example.management.models.enums.LeaveTypeEnum;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LeaveRequestCreateRequest {
private long userId;
private LeaveTypeEnum leaveType;
private LocalDate startDate;
private LocalDate endDate;
private String description;
private String reports;
}
