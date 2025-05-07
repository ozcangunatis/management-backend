package com.example.management.dto;

import lombok.Data;

@Data
public class ResetPasswordRequestDTO {
    private String email;
    private String newPassword;
}
