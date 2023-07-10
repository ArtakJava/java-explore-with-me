package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.pageParameter.PageRequestCustom;
import ru.practicum.constantManager.ConstantManager;
import ru.practicum.dto.user.UserDto;
import ru.practicum.messageManager.InfoMessageManager;
import ru.practicum.service.admin.user.UserAdminService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@Slf4j
@RequiredArgsConstructor
public class UserAdminController {
    private final UserAdminService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        log.info(InfoMessageManager.POST_REQUEST, userDto);
        return service.create(userDto);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long userId) {
        log.info(InfoMessageManager.DELETE_REQUEST, userId);
        service.delete(userId);
    }

    @GetMapping
    public List<UserDto> getUsers(
            @RequestParam(required = false) long[] ids,
            @RequestParam(defaultValue = ConstantManager.DEFAULT_PAGE_PARAMETER_FROM) @PositiveOrZero int from,
            @RequestParam(defaultValue = ConstantManager.DEFAULT_SIZE_OF_PAGE_USERS) @Positive int size) {
        log.info(InfoMessageManager.GET_ALL_USERS_REQUEST);
        return service.getUsers(ids, new PageRequestCustom(from, size, ConstantManager.SORT_USERS_BY_ID_ASC));
    }
}