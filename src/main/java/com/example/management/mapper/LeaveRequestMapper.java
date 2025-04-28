package com.example.management.mapper;

import com.example.management.request.LeaveRequestCreateRequest;
import com.example.management.response.LeaveRequestResponse;
import com.example.management.models.LeaveRequest;
import com.example.management.models.LeaveType;
import com.example.management.models.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LeaveRequestMapper {

    public LeaveRequestResponse toDto(LeaveRequest leaveRequest) {
        if (leaveRequest == null) return null;

        LeaveRequestResponse dto = new LeaveRequestResponse();
        dto.setId(leaveRequest.getId());
        dto.setUserId(leaveRequest.getUser().getId());
        dto.setUserFullName(leaveRequest.getUser().getFirstName() + " " + leaveRequest.getUser().getLastName());
        dto.setLeaveType(leaveRequest.getLeaveType().getLeaveType());
        dto.setStartDate(leaveRequest.getStartDate());
        dto.setEndDate(leaveRequest.getEndDate());
        dto.setStatus(leaveRequest.getStatus().name());
        dto.setDescription(leaveRequest.getDescription());
        dto.setReports(leaveRequest.getReports());
        dto.setCreatedAt(leaveRequest.getCreatedAt());
        return dto;
    }

    public List<LeaveRequestResponse> toDtoList(List<LeaveRequest> requests) {
        return requests.stream().map(this::toDto).collect(Collectors.toList());
    }

    public LeaveRequest toEntity(LeaveRequestCreateRequest request, User user, LeaveType leaveType) {
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setUser(user);
        leaveRequest.setLeaveType(leaveType);
        leaveRequest.setStartDate(request.getStartDate());
        leaveRequest.setEndDate(request.getEndDate());
        leaveRequest.setDescription(request.getDescription());
        leaveRequest.setReports(request.getReports());
        return leaveRequest;
    }
}
