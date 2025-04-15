package com.example.management.controller;

import com.example.management.dto.UserCreateDto;
import com.example.management.dto.UserDto;
import com.example.management.dto.UserUpdateDto;
import com.example.management.models.Enum.Role;
import com.example.management.response.GetUsersResponse;
import com.example.management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    // tüm kullanıcıları getir
    @GetMapping
    public ResponseEntity<?> getUsers(@RequestParam(required = false) Role role) {
        if (role != null) {
            return ResponseEntity.ok(userService.getUsersByRole(role));
        }
        return ResponseEntity.ok(userService.getUsers());
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        Optional<UserDto> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    // yeni kullanıcı ekleme
    @PostMapping
    public ResponseEntity<UserDto> addUser(@RequestBody UserCreateDto userDto) {
        return ResponseEntity.ok(userService.addUser(userDto));
    }
    // kullanıcı güncelleme
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserUpdateDto userUpdateDto) {
        Optional<UserDto> updated = userService.updateUser(id, userUpdateDto);
        return updated.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok("User deleted successfully");
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("User Not Found.");
        }
    }

}
