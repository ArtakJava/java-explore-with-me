package ru.practicum.service.admin.user;

import ru.practicum.dto.event.pageParameter.PageRequestCustom;
import ru.practicum.dto.user.UserDto;

import java.util.List;

public interface UserAdminService {

    UserDto create(UserDto userDto);

    void delete(long userId);

    List<UserDto> getUsers(long[] ids, PageRequestCustom pageRequest);
}