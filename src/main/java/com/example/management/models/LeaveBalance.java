package com.example.management.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class LeaveBalance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
    @Column(nullable = false)
    private Integer totalDays;
    @Column(nullable = false)
    private Integer usedDays; // kullanilmis izin gunleri
    @Column(nullable = false)
    private Integer remainingDays; // kalan izin gunleri
    @PrePersist
    @PreUpdate
    public void calculateRemainingDays() {
        this.remainingDays = totalDays - usedDays;
    }

}
