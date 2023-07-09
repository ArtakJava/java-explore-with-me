package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.EventAdminPageParameter;
import ru.practicum.PageRequestCustom;
import ru.practicum.constantManager.ConstantManager;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.messageManager.InfoMessageManager;
import ru.practicum.service.admin.event.EventAdminService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@Slf4j
@RequiredArgsConstructor
public class EventAdminController {
    private final EventAdminService service;

    @PatchMapping("/{eventId}")
    public EventFullDto update(@PathVariable long eventId, @Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        log.info(InfoMessageManager.PATCH_REQUEST_EVENT, eventId, updateEventAdminRequest);
        return service.update(eventId, updateEventAdminRequest);
    }

    @GetMapping
    public List<EventFullDto> getEvents(@RequestParam(required = false) long[] users,
                                        @RequestParam(required = false) String[] states,
                                        @RequestParam(required = false) long[] categories,
                                        @RequestParam(required = false) @DateTimeFormat(pattern = ConstantManager.DATE_PATTERN) LocalDateTime rangeStart,
                                        @RequestParam(required = false) @DateTimeFormat(pattern = ConstantManager.DATE_PATTERN) LocalDateTime rangeEnd,
                                        @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = ConstantManager.DEFAULT_SIZE_OF_PAGE_EVENTS) int size) {
        log.info(InfoMessageManager.GET_ALL_USERS_REQUEST);
        return service.getEvents(
                new EventAdminPageParameter(
                        users,
                        states,
                        categories,
                        rangeStart,
                        rangeEnd,
                        new PageRequestCustom(from, size, ConstantManager.SORT_EVENTS_BY_EVENT_DATE)
                )
        );
    }
}