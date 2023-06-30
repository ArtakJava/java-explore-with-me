package ru.practicum.service.admin.user;

import ru.practicum.dto.user.UserDto;
import ru.practicum.model.User;

public class UserMapper {

    public static User mapToUserEntity(UserDto userDto) {
        return User.builder()
                .email(userDto.getEmail())
                .name(userDto.getName())
                .build();
    }

    public static UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .email(user.getEmail())
                .name(user.getName())
                .build();
    }
}