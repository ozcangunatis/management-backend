package com.example.management.controller;

import com.example.management.auth.AuthenticationRequest;
import com.example.management.auth.AuthenticationResponse;
import com.example.management.dto.ForgotPasswordRequestDTO;
import com.example.management.dto.ResetPasswordRequestDTO;
import com.example.management.repositories.UserRepository;
import com.example.management.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import com.example.management.model.User;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = authenticationService.authenticate(request);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequestDTO request){
        Optional<User> optionalUser = userRepository.findByEmailAndTcNoAndPhoneNumber(
                request.getEmail(), request.getTcNo(), request.getPhoneNumber()
        );
        if(optionalUser.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email or TcNo");
        }

        return ResponseEntity.ok("Information is correct.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequestDTO request){
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

        if(optionalUser.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("e-mail is not registered in the system");
        }
        User user = optionalUser.get();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("Password reset successful.");
    }
}
