package com.example.management.request;

import com.example.management.model.enums.LeaveTypeEnum;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LeaveRequestCreateRequest {
private LeaveTypeEnum leaveTypeEnum;
private LocalDate startDate;
private LocalDate endDate;
private String description;
private String reports;
}
