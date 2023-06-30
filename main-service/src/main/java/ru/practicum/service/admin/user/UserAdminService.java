package ru.practicum.service.admin.user;

import ru.practicum.PageRequestCustom;
import ru.practicum.dto.user.UserDto;

import java.util.List;

public interface UserAdminService {

    UserDto create(UserDto userDto);

    void delete(long userId);

    List<UserDto> getUsers(int[] ids, PageRequestCustom pageRequest);
}