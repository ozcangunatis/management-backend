package com.example.management.mapper;

import com.example.management.dto.UserCreateDto;
import com.example.management.dto.UserDto;
import com.example.management.dto.UserUpdateDto;
import com.example.management.models.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        if (user == null) return null;

        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole()
        );
    }

    public List<UserDto> toDtoList(List<User> users) {
        if (users == null) return null;
        return users.stream().map(this::toDto).collect(Collectors.toList());
    }

    public User toEntity(UserCreateDto dto) {
        if (dto == null) return null;

        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());
        return user;
    }

    public void updateEntity(User user, UserUpdateDto dto) {
        if (user == null || dto == null) return;

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        // password değiştirilmediyse dokunulmaz
    }
}
