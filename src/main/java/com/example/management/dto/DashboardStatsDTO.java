package com.example.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
    private int newLeaveRequests;
    private int pendingLeaveRequests;
    private int leaveDaysThisMonth;
    private int employeesOnLeaveThisMonth;
}
