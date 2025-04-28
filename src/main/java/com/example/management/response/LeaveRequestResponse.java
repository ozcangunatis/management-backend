package com.example.management.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class LeaveRequestResponse {
    private Long id;
    private Long userId;
    private String userFullName;
    private String leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private String description;
    private String reports;
    private LocalDateTime createdAt;
}
