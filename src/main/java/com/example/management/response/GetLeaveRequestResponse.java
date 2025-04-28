package com.example.management.response;

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
    private List<LeaveRequestResponse> leaveRequests;
}
