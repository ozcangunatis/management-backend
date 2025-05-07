package com.example.management.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "leave_types")
@Getter
@Setter
public class LeaveType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_leave_type")
    private Long id;

    @Column(name = "leave_type", nullable = false)
    private String leaveType;

    @OneToMany(mappedBy = "leaveType", cascade = CascadeType.ALL)
    private List<LeaveRequest> leaveRequests;

    @OneToMany(mappedBy = "leaveType", cascade = CascadeType.ALL)
    private List<LeaveBalance> leaveBalances;
}
