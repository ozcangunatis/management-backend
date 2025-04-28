package com.example.management.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LeaveAnswerResponse {
    private Long id;
    private Long leaveRequestId;
    private Long repliedById;
    private String repliedByName;
    private String replyStatus;
    private LocalDate replyDate;
}
