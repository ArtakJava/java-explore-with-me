package ru.practicum.service.admin.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.PageRequestCustom;
import ru.practicum.dto.user.UserDto;
import ru.practicum.messageManager.InfoMessageManager;
import ru.practicum.model.User;
import ru.practicum.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<UserDto> getUsers(int[] ids, PageRequestCustom pageRequest) {
        List<User> users = userRepository.findAllByIdIn(ids, pageRequest);
        return users.stream()
                .map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());
    }
}