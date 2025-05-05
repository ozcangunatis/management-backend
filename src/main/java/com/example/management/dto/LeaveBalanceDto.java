package com.example.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveBalanceDto {
    private Long userId;
    private Integer totalDays;
    private Integer usedDays;
    private Integer remainingDays;

    private int annualLeaveThisYear;
    private int carriedOverFromLastYear;
    private int totalAvailableDays;

}
