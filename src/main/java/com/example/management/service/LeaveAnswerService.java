package com.example.management.service;

import com.example.management.mapper.LeaveAnswerMapper;
import com.example.management.models.LeaveAnswer;
import com.example.management.models.LeaveRequest;
import com.example.management.models.User;
import com.example.management.models.enums.LeaveStatus;
import com.example.management.repositories.LeaveAnswerRepository;
import com.example.management.repositories.LeaveRequestRepository;
import com.example.management.repositories.UserRepository;
import com.example.management.response.LeaveAnswerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class LeaveAnswerService {

    private final LeaveAnswerRepository leaveAnswerRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final UserRepository userRepository;
    private final LeaveAnswerMapper leaveAnswerMapper;

    public LeaveAnswerResponse approveLeaveRequest(Long requestId, String approverEmail) {
        return handleAnswer(requestId, approverEmail, LeaveStatus.APPROVED);
    }

    public LeaveAnswerResponse rejectLeaveRequest(Long requestId, String approverEmail) {
        return handleAnswer(requestId, approverEmail, LeaveStatus.REJECTED);
    }

    private LeaveAnswerResponse handleAnswer(Long requestId, String approverEmail, LeaveStatus status) {
        User approver = userRepository.findByEmail(approverEmail)
                .orElseThrow(() -> new IllegalArgumentException("Approver not found"));

        LeaveRequest request = leaveRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Leave request not found"));


        if (approver.getRole().name().equals("HR")) {
            if (!request.getUser().getRole().name().equals("EMPLOYEE")) {
                throw new AccessDeniedException("HR can only respond to EMPLOYEE leave requests.");
            }
        }

        if (request.getStatus() != LeaveStatus.PENDING) {
            throw new IllegalStateException("Only PENDING requests can be answered.");
        }


        LeaveAnswer answer = new LeaveAnswer();
        answer.setLeaveRequest(request);
        answer.setRepliedBy(approver);
        answer.setReplyStatus(status);
        answer.setReplyDate(LocalDate.now());

        leaveAnswerRepository.save(answer);

        request.setStatus(status);
        leaveRequestRepository.save(request);

        return leaveAnswerMapper.toDto(answer);
    }
}
