package com.example.management.dto;

import lombok.Data;

import java.time.LocalDate;
@Data
public class UpdateLeaveRequestDatesRequest {
    private LocalDate startDate;
    private LocalDate endDate;
}
