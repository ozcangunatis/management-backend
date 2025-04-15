package com.example.management.response;

import com.example.management.models.Enum.LeaveStatus;
import com.example.management.models.Enum.LeaveType;
import lombok.Data;

import java.time.LocalDate;
@Data
public class LeaveRequestResponse {
    private long id;
    private long userId;
    private LeaveType leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private LeaveStatus leaveStatus;
}
