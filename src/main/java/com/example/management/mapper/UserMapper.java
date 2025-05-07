package com.example.management.mapper;

import com.example.management.dto.UserCreateDto;
import com.example.management.dto.UserDto;
import com.example.management.dto.UserUpdateDto;
import com.example.management.model.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        if (user == null) return null;

        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setTcNo(user.getTcNo());
        dto.setAddress(user.getAddress());
        dto.setGender(user.getGender());
        dto.setOfficeId(user.getOffice() != null ? user.getOffice().getId() : null);
        dto.setJobEntryDate(user.getJobEntryDate());
        return dto;
    }

    public List<UserDto> toDtoList(List<User> users) {
        if (users == null) return null;
        return users.stream().map(this::toDto).collect(Collectors.toList());
    }

    public User toEntity(UserCreateDto dto) {
        if (dto == null) return null;

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setRole(dto.getRole());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setTcNo(dto.getTcNo());
        user.setAddress(dto.getAddress());
        user.setGender(dto.getGender());
        user.setJobEntryDate(dto.getJobEntryDate());
        return user;
    }

    public void updateEntity(User user, UserUpdateDto dto) {
        if (user == null || dto == null) return;

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());

    }
}
