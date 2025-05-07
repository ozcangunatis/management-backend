package com.example.management.controller;

import com.example.management.dto.UserCreateDto;
import com.example.management.dto.UserDto;
import com.example.management.dto.UserUpdateDto;
import com.example.management.mapper.UserMapper;
import com.example.management.model.User;
import com.example.management.model.enums.Role;
import com.example.management.repositories.UserRepository;
import com.example.management.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    private User getCurrentAuthenticatedUser() {
        return userService.getCurrentAuthenticatedUser();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_HR', 'HR')")
    public ResponseEntity<UserDto> addUser(@Valid @RequestBody UserCreateDto userDto) {
        User currentUser = getCurrentAuthenticatedUser();


        if (currentUser.getRole() == Role.ADMIN || currentUser.getRole() == Role.SUPER_HR) {
            UserDto createdUser = userService.addUser(userDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        }


        if (currentUser.getRole() == Role.HR) {
            if (userDto.getRole() != Role.EMPLOYEE) {

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
            if (userDto.getOfficeId() == null || !userDto.getOfficeId().equals(currentUser.getOffice().getId())) {

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            UserDto createdUser = userService.addUser(userDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        }


        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }




    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'SUPER_HR')")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.getUserById(id);
        return user.map(u -> userMapper.toDto(u))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }



    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_HR', 'HR')")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserUpdateDto userUpdateDto) {
        User currentUser = getCurrentAuthenticatedUser();

        // HR sadece kendi ofisindeki kullanıcıyı güncelleyebilir
        if (currentUser.getRole() == Role.HR) {
            User existingUser = userRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            if (existingUser.getOffice() == null || !existingUser.getOffice().getId().equals(currentUser.getOffice().getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        }


        User updatedUser = userService.updateUser(id, userUpdateDto);
        UserDto userDto = userMapper.toDto(updatedUser);

        return ResponseEntity.ok(userDto);
    }




    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_HR', 'HR')")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        try {
            User currentUser = getCurrentAuthenticatedUser();


            if (currentUser.getRole() == Role.HR) {
                User targetUser = userService.getUserById(userId)
                        .orElseThrow(() -> new IllegalArgumentException("User not found"));


                if (targetUser.getOffice() == null || !targetUser.getOffice().getId().equals(currentUser.getOffice().getId())) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("HR can only delete users from their own office.");
                }
            }

            userService.deleteUser(userId);
            return ResponseEntity.ok("User deleted successfully");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("User not found.");
        }
    }

}



