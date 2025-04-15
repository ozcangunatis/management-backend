package com.example.management.auth;

import lombok.Data;


@Data



public class AuthenticationRequest {
    private String email;
    private String password;

}
