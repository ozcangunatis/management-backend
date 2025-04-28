package com.example.management.models;

import com.example.management.models.enums.Role;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Collection;
import java.util.Collections;

@Entity
@Data
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private long id;

    @Column(nullable = false , length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();


    @Column(name = "job_entry_date", nullable = true)
    private LocalDate jobEntryDate;

    private String gender;

    @ManyToOne
    @JoinColumn(name = "office_id", nullable = true)
    private Office office;


    @Column(length = 11, unique = true)
    private String tcNo;

    @Column(length = 15)
    private String phoneNumber;

    @Column
    private String address;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<LeaveRequest> leaveRequests;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(
                new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + role.name())
        );
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
