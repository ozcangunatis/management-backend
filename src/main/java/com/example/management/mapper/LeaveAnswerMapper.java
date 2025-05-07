package com.example.management.mapper;

import com.example.management.model.LeaveAnswer;
import com.example.management.response.LeaveAnswerResponse;
import org.springframework.stereotype.Component;

@Component
public class LeaveAnswerMapper {

    public LeaveAnswerResponse toDto(LeaveAnswer entity) {
        if (entity == null)
            return null;

        LeaveAnswerResponse dto = new LeaveAnswerResponse();
        dto.setId(entity.getId());
        dto.setLeaveRequestId(entity.getLeaveRequest().getId());
        dto.setRepliedById(entity.getRepliedBy().getId());
        dto.setRepliedByName(entity.getRepliedBy().getFirstName() + " " + entity.getRepliedBy().getLastName());
        dto.setReplyStatus(entity.getReplyStatus().name());
        dto.setReplyDate(entity.getReplyDate());
        return dto;
    }
}
