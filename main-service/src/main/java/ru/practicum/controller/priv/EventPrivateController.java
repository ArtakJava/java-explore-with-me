package ru.practicum.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.PageRequestCustom;
import ru.practicum.constantManager.ConstantManager;
import ru.practicum.dto.event.*;
import ru.practicum.messageManager.InfoMessageManager;
import ru.practicum.service.priv.event.EventPrivateService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class EventPrivateController {
    private final EventPrivateService service;

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable long userId, @Valid @RequestBody NewEventDto newEventDto) {
        log.info(InfoMessageManager.POST_REQUEST, newEventDto);
        return service.create(userId, newEventDto);
    }

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getAllUserEvents(@PathVariable long userId,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = ConstantManager.DEFAULT_SIZE_OF_PAGE_EVENTS) int size) {
        log.info(InfoMessageManager.GET_USER_ALL_EVENTS_REQUEST, userId);
        return service.getAllUserEvents(userId, new PageRequestCustom(from, size, ConstantManager.SORT_EVENTS_BY_EVENT_DATE));
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getUserEvent(@PathVariable long userId, @PathVariable long eventId) {
        log.info(InfoMessageManager.GET_USER_EVENT_REQUEST, eventId);
        return service.getUserEvent(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto patchUserEvent(@PathVariable long userId,
                                       @PathVariable long eventId,
                                       @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        log.info(InfoMessageManager.PATCH_REQUEST, eventId, updateEventUserRequest);
        return service.patchUserEvent(userId, eventId,updateEventUserRequest);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getAllUserEventRequests(@PathVariable long userId, @PathVariable long eventId) {
        log.info(InfoMessageManager.GET_USER_EVENT_REQUESTS, eventId);
        return service.getAllUserEventRequests(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult approveRequestsByEvent(@PathVariable long userId,
                                                                 @PathVariable long eventId,
                                                                 @Valid @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.info(InfoMessageManager.GET_REQUEST_APPROVE_REQUESTS, eventId);
        return service.approveRequestsByEvent(userId, eventId,eventRequestStatusUpdateRequest);
    }
}