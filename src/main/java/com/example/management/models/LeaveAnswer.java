package com.example.management.models;

import com.example.management.models.enums.LeaveStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "leave_answers")
public class LeaveAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_leave_answer")
    private Long id;


    @OneToOne
    @JoinColumn(name = "leave_request_id", nullable = false)
    private LeaveRequest leaveRequest;


    @ManyToOne
    @JoinColumn(name = "replied_by", nullable = false)
    private User repliedBy;

    @Column(name = "reply_date", nullable = false)
    private LocalDate replyDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "reply_status", nullable = false)
    private LeaveStatus replyStatus; // APPROVED or REJECTED
}
