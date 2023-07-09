package ru.practicum.service.admin.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.event.pageParameter.PageRequestCustom;
import ru.practicum.dto.user.UserDto;
import ru.practicum.messageManager.InfoMessageManager;
import ru.practicum.model.User;
import ru.practicum.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserAdminServiceImpl implements UserAdminService {
    private final UserRepository userRepository;

    @Override
    public UserDto create(UserDto userDto) {
        User user = UserMapper.mapToUserEntity(userDto);
        UserDto result = UserMapper.mapToUserDto(userRepository.save(user));
        log.info(InfoMessageManager.SUCCESS_CREATE, result);
        return result;
    }

    @Override
    public void delete(long userId) {
        User user = userRepository.getReferenceById(userId);
        userRepository.delete(user);
        log.info(InfoMessageManager.SUCCESS_DELETE, user);
    }

    @Override
    public List<UserDto> getUsers(long[] ids, PageRequestCustom pageRequest) {
        List<User> users;
        if (ids != null) {
            users = userRepository.findAllByIdIn(ids, pageRequest);
        } else {
            users = userRepository.findAll(pageRequest).toList();
        }
        log.info(InfoMessageManager.SUCCESS_ALL_USERS_REQUEST);
        return UserMapper.mapToUsersDto(users);
    }
}