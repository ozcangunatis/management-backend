package com.example.management.dto;

import com.example.management.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Role role;
    private String phoneNumber;
    private String tcNo;
    private String address;
    private String gender;
    private Long officeId;
    private LocalDate jobEntryDate;
}
