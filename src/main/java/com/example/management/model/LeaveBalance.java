package com.example.management.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "leave_balances")
public class LeaveBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_leave_balance")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "leave_type_id", nullable = false)
    private LeaveType leaveType;

    @Column(name = "leave_year", nullable = false)
    private int leaveYear;

    @Column(name = "annual_leave_days", nullable = false)
    private int annualLeaveDays;

    @Column(name = "used_days", nullable = false)
    private int usedDays;

    @Column(name = "remaining_days", nullable = false)
    private int remainingDays;



    @PrePersist
    @PreUpdate
    public void calculateRemainingDays() {
        this.remainingDays = this.annualLeaveDays - this.usedDays;
    }
}
