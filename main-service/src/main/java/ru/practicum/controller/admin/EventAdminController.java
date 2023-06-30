package ru.practicum.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.EventPageParameter;
import ru.practicum.PageRequestCustom;
import ru.practicum.constantManager.ConstantManager;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.messageManager.InfoMessageManager;
import ru.practicum.service.admin.event.EventAdminService;

import javax.validation.Valid;
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
    public List<EventFullDto> getEvents(@RequestParam int[] ids,
                                        @RequestParam String[] states,
                                        @RequestParam int[] categories,
                                        @RequestParam String rangeStart,
                                        @RequestParam String rangeEnd,
                                        @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = ConstantManager.DEFAULT_SIZE_OF_PAGE_EVENTS) int size) {
        log.info(InfoMessageManager.GET_ALL_USERS_REQUEST);
        return service.getEvents(
                new EventPageParameter(
                        ids,
                        states,
                        categories,
                        rangeStart,
                        rangeEnd,
                        new PageRequestCustom(from, size, ConstantManager.SORT_EVENTS_BY_ID_DESC)
                )
        );
    }
}