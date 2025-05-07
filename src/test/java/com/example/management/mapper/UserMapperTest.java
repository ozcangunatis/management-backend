package com.example.management.mapper;

import com.example.management.dto.UserCreateDto;
import com.example.management.dto.UserDto;
import com.example.management.model.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapperTest {


    UserDto userToUserDto(User user);





    List<UserDto> usersToUserDtos(List<User> users);



    User userCreateDtoToUser(UserCreateDto userCreateDto);
}
