package com.example.management.request;

import com.example.management.models.Enum.LeaveType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LeaveRequestCreateRequest {
private long userId;
private LeaveType leaveType;
private LocalDate startDate;
private LocalDate endDate;
}
