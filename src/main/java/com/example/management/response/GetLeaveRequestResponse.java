package com.example.management.response;

import com.example.management.dto.LeaveRequestDto;
import com.example.management.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)

public class GetLeaveRequestResponse extends BaseResponse {
    private List<LeaveRequestDto> leaveRequests;
}
