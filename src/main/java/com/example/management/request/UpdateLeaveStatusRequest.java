package com.example.management.request;

import com.example.management.model.enums.LeaveStatus;
import lombok.Data;
//İzin isteğinin durumunu güncellemek
@Data
public class UpdateLeaveStatusRequest {
    private long requestId;
    private LeaveStatus newStatus;
}
