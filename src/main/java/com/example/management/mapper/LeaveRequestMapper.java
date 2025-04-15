package com.example.management.mapper;

import com.example.management.dto.LeaveRequestDto;
import com.example.management.models.LeaveRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LeaveRequestMapper {

    public LeaveRequestDto toDto(LeaveRequest leaveRequest) {
        if (leaveRequest == null) return null;

        return new LeaveRequestDto(
                leaveRequest.getId(),
                leaveRequest.getUser().getId(),
                leaveRequest.getLeaveType(),
                leaveRequest.getStartDate(),
                leaveRequest.getEndDate(),
                leaveRequest.getStatus()
        );
    }

    public List<LeaveRequestDto> toDtoList(List<LeaveRequest> requests) {
        return requests.stream().map(this::toDto).collect(Collectors.toList());
    }
}
