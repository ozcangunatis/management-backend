package com.example.management.dto;

import lombok.Data;

@Data
public class ForgotPasswordRequestDTO {
    private String email;
    private String tcNo;
    private String phoneNumber;
}
