package com.example.management.dto;

import com.example.management.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {
    private String firstName;
    private String lastName;
    private String email;
    private String password;  // Şifre güncellenmesi için gerekli alan
    private Role role;
}
