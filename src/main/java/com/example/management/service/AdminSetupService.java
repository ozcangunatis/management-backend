package com.example.management.service;

import com.example.management.model.User;
import com.example.management.model.enums.Role;
import com.example.management.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminSetupService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;


    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.firstName}")
    private String adminFirstName;

    @Value("${admin.lastName}")
    private String adminLastName;

    @Bean
    public CommandLineRunner setupAdminUser() {
        return args -> {
            if (!userRepository.existsByEmail(adminEmail)) {
                User admin = new User();
                admin.setEmail(adminEmail);
                admin.setFirstName(adminFirstName);
                admin.setLastName(adminLastName);
                admin.setPassword(passwordEncoder.encode(adminPassword));
                admin.setRole(Role.ADMIN);
                userRepository.save(admin);
                System.out.println("Admin user created!");
            } else {
                System.out.println("Admin user already exists!");
            }
        };
    }
}
