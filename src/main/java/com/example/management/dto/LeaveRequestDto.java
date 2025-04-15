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

public class LeaveRequestDto {
    private Long id;
    private Long userId;
    private LeaveType leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private LeaveStatus status;

}
