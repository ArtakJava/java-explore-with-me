package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.constantManager.ConstantManager;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.dto.event.pageParameter.EventAdminPageParameter;
import ru.practicum.dto.event.pageParameter.PageRequestCustom;
import ru.practicum.messageManager.InfoMessageManager;
import ru.practicum.service.admin.event.EventAdminService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@Slf4j
@RequiredArgsConstructor
@Validated
public class EventAdminController {
    private final EventAdminService service;

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable long eventId, @Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        log.info(InfoMessageManager.PATCH_REQUEST_EVENT, eventId, updateEventAdminRequest);
        return service.updateEvent(eventId, updateEventAdminRequest);
    }

    @GetMapping
    public List<EventFullDto> getEvents(
            @RequestParam(required = false) long[] users,
            @RequestParam(required = false) String[] states,
            @RequestParam(required = false) long[] categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = ConstantManager.DATE_PATTERN) LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = ConstantManager.DATE_PATTERN) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = ConstantManager.DEFAULT_PAGE_PARAMETER_FROM) @PositiveOrZero int from,
            @RequestParam(defaultValue = ConstantManager.DEFAULT_SIZE_OF_PAGE_EVENTS) @Positive int size) {
        log.info(InfoMessageManager.GET_ALL_USERS_REQUEST);
        PageRequestCustom pageRequest = new PageRequestCustom(from, size, ConstantManager.SORT_EVENTS_BY_EVENT_DATE);
        return service.getEvents(
                new EventAdminPageParameter(
                        users,
                        states,
                        categories,
                        rangeStart,
                        rangeEnd,
                        pageRequest
                )
        );
    }
}