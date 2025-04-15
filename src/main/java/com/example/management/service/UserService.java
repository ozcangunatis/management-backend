package com.example.management.service;

import com.example.management.dto.UserCreateDto;
import com.example.management.dto.UserDto;
import com.example.management.dto.UserUpdateDto;
import com.example.management.mapper.UserMapper;
import com.example.management.models.Enum.Role;
import com.example.management.models.User;
import com.example.management.repositories.UserRepository;
import com.example.management.response.GetUsersResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    public GetUsersResponse getUsers() {
        GetUsersResponse response = new GetUsersResponse();
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            response.setMessage("No users found");
            response.setStatus("error");
            response.setStatusCode(404);
            return response;
        }

        List<UserDto> userDtos = userMapper.toDtoList(users);


        response.setMessage("Success");
        response.setStatus("success");
        response.setStatusCode(200);
        response.setUsers(userDtos);

        return response;
    }


    public Optional<UserDto> getUserById(long id) {
        return userRepository.findById(id).map(userMapper::toDto);

    }


    @Transactional
    public UserDto addUser(UserCreateDto userCreateDto) {
        if (userCreateDto.getPassword() == null || userCreateDto.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty!");
        }

        log.info("Request DTO user: " + userCreateDto);

        User user = userMapper.toEntity(userCreateDto);


        User savedUser = new User();
        savedUser.setEmail(userCreateDto.getEmail());

        user.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        log.info("User: " + user);
        user = userRepository.save(user);

        return userMapper.toDto(user);

    }

        @Transactional
        public Optional<UserDto> updateUser(Long id, UserUpdateDto updatedUserDto) {
            return userRepository.findById(id).map(existingUser -> {
                if (updatedUserDto.getFirstName() != null) {
                    existingUser.setFirstName(updatedUserDto.getFirstName());
                }
                if (updatedUserDto.getLastName() != null) {
                    existingUser.setLastName(updatedUserDto.getLastName());
                }
                if (updatedUserDto.getEmail() != null&& !existingUser.getEmail().equals(updatedUserDto.getEmail())) {
                    if(userRepository.existsByEmail(updatedUserDto.getEmail())) {
                        throw new IllegalArgumentException("Email already exists!");
                    }
                    existingUser.setEmail(updatedUserDto.getEmail());
                }
                // Şifre güncelleme işlemi
                if (updatedUserDto.getPassword() != null) {
                    existingUser.setPassword(passwordEncoder.encode(updatedUserDto.getPassword()));
                }

                userRepository.save(existingUser);
                return userMapper.toDto(existingUser);

            });
        }

    public void deleteUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found!");
        }
        userRepository.deleteById(userId);
    }
    public List<UserDto> getUsersByRole(Role role) {
        List<User> users = userRepository.findByRole(role);
        return users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }



}
