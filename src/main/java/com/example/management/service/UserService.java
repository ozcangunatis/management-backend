package com.example.management.service;

import com.example.management.dto.*;
import com.example.management.mapper.UserMapper;
import com.example.management.model.Office;
import com.example.management.model.enums.Role;
import com.example.management.model.User;
import com.example.management.repositories.OfficeRepository;
import com.example.management.repositories.UserRepository;
import com.example.management.response.GetUsersResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final OfficeRepository officeRepository;




    public GetUsersResponse getUsers() {
        User currentUser = getCurrentAuthenticatedUser();
        List<User> users;

        if (currentUser.getRole() == Role.HR && currentUser.getOffice() != null) {
            users = userRepository.findByOfficeId(currentUser.getOffice().getId());
        } else {
            users = userRepository.findAll();
        }

        GetUsersResponse response = new GetUsersResponse();

        if (users.isEmpty()) {
            response.setMessage("No users found");
            response.setStatus("error");
            response.setStatusCode(404);
            return response;
        }

        response.setUsers(userMapper.toDtoList(users));
        response.setStatus("success");
        response.setMessage("Success");
        response.setStatusCode(200);
        return response;
    }




    public Optional<User> getUserById(long id) {
        return userRepository.findById(id);
    }





    @Transactional
    public UserDto addUser(UserCreateDto userCreateDto) {
        User currentUser = getCurrentAuthenticatedUser();

        if (userCreateDto.getPassword() == null || userCreateDto.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty!");
        }

        // HR
        if (currentUser.getRole() == Role.HR && userCreateDto.getRole() != Role.EMPLOYEE) {
            throw new AccessDeniedException("HR can only create EMPLOYEE users.");
        }

        // SUPER_HR
        if (currentUser.getRole() == Role.SUPER_HR &&
                !(userCreateDto.getRole() == Role.EMPLOYEE || userCreateDto.getRole() == Role.HR)) {
            throw new AccessDeniedException("SUPER_HR can only create EMPLOYEE and HR users.");
        }

        if (currentUser.getRole() == Role.HR) {
            if (userCreateDto.getOfficeId() == null || !userCreateDto.getOfficeId().equals(currentUser.getOffice().getId())) {
                throw new AccessDeniedException("HR can only create users for their own office.");
            }
        }

        log.info("Request DTO user: " + userCreateDto);
        log.info("DTO'dan gelen jobEntryDate: " + userCreateDto.getJobEntryDate());


        User user = new User();
        user.setEmail(userCreateDto.getEmail());
        user.setFirstName(userCreateDto.getFirstName());
        user.setLastName(userCreateDto.getLastName());
        user.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        user.setRole(userCreateDto.getRole());
        user.setPhoneNumber(userCreateDto.getPhoneNumber());
        user.setTcNo(userCreateDto.getTcNo());
        user.setAddress(userCreateDto.getAddress());
        user.setGender(userCreateDto.getGender());
        user.setJobEntryDate(userCreateDto.getJobEntryDate());

        if (userCreateDto.getOfficeId() != null) {
            Office office = officeRepository.findById(userCreateDto.getOfficeId())
                    .orElseThrow(() -> new RuntimeException("Office not found"));
            user.setOffice(office);
        }

        log.info("User (mapped + password + full fields): " + user);

        user = userRepository.save(user);

        return userMapper.toDto(user);
    }

    @Transactional
    public User updateUser(Long id, UserUpdateDto updatedUserDto) {
        User currentUser = getCurrentAuthenticatedUser();

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));


        if (currentUser.getRole() == Role.HR) {
            if (currentUser.getOffice() == null || existingUser.getOffice() == null ||
                    !currentUser.getOffice().getId().equals(existingUser.getOffice().getId())) {
                throw new AccessDeniedException("HR can only update users from their own office.");
            }
        }




        if (updatedUserDto.getFirstName() != null) {
            existingUser.setFirstName(updatedUserDto.getFirstName());
        }
        if (updatedUserDto.getLastName() != null) {
            existingUser.setLastName(updatedUserDto.getLastName());
        }
        if (updatedUserDto.getEmail() != null && !existingUser.getEmail().equals(updatedUserDto.getEmail())) {
            if (userRepository.existsByEmail(updatedUserDto.getEmail())) {
                throw new IllegalArgumentException("Email already exists!");
            }
            existingUser.setEmail(updatedUserDto.getEmail());
        }
        if (updatedUserDto.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(updatedUserDto.getPassword()));
        }

        userRepository.save(existingUser);
        return existingUser;
    }




    public void deleteUser(Long userId) {
        User currentUser = getCurrentAuthenticatedUser();
        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));


        if (currentUser.getRole() == Role.HR) {
            if (currentUser.getOffice() == null || targetUser.getOffice() == null ||
                    !currentUser.getOffice().getId().equals(targetUser.getOffice().getId())) {
                throw new AccessDeniedException("HR can only delete users from their own office.");
            }
        }


        userRepository.deleteById(userId);
    }




    public List<UserDto> getUsersByRole(Role role) {
        User currentUser = getCurrentAuthenticatedUser();
        List<User> users;

        if (currentUser.getRole() == Role.HR && currentUser.getOffice() != null) {
            users = userRepository.findByRole(role).stream()
                    .filter(user -> user.getOffice() != null &&
                            user.getOffice().getId().equals(currentUser.getOffice().getId()))
                    .collect(Collectors.toList());
        }

        else if (currentUser.getRole() == Role.SUPER_HR) {
            users = userRepository.findByRole(role).stream()
                    .filter(user -> user.getOffice() != null)
                    .collect(Collectors.toList());
        }

        else {
            users = userRepository.findByRole(role);
        }

        return users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    public User getCurrentAuthenticatedUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("No authenticated user found.");
        }

        String email = authentication.getName();



        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));


        System.out.println("Active User" + user.getEmail() + " | Rolü: " + user.getRole());

        return user;
    }
    public boolean resetPassword(ResetPasswordRequestDTO request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isEmpty()) {
            return false;
        }
        User user = optionalUser.get();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return true;
    }

    public boolean validateForgotPasswordInfo(ForgotPasswordRequestDTO request) {
        return userRepository.findByEmailAndTcNoAndPhoneNumber(
                request.getEmail(),
                request.getTcNo(),
                request.getPhoneNumber()
        ).isPresent();
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getUsersByOfficeId(Long officeId) {
        return userRepository.findAllByOfficeId(officeId);
    }



}
