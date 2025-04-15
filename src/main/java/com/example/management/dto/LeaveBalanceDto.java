package com.example.management.dto;

import com.example.management.models.Enum.LeaveStatus;
import com.example.management.models.Enum.LeaveType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveBalanceDto {
    private Long userId;
    private Integer totalDays;
    private Integer usedDays;
    private Integer remainingDays;

}
