package ru.practicum.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.constantManager.ConstantManager;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.dto.event.pageParameter.PageRequestCustom;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.messageManager.InfoMessageManager;
import ru.practicum.service.priv.event.EventPrivateService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@Slf4j
@RequiredArgsConstructor
@Validated
public class EventPrivateController {
    private final EventPrivateService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable long userId, @Valid @RequestBody NewEventDto newEventDto) {
        log.info(InfoMessageManager.POST_REQUEST, newEventDto);
        return service.create(userId, newEventDto);
    }

    @GetMapping
    public List<EventShortDto> getAllUserEvents(
            @PathVariable long userId,
            @RequestParam(defaultValue = ConstantManager.DEFAULT_PAGE_PARAMETER_FROM) @PositiveOrZero int from,
            @RequestParam(defaultValue = ConstantManager.DEFAULT_SIZE_OF_PAGE_EVENTS) @Positive int size) {
        log.info(InfoMessageManager.GET_USER_ALL_EVENTS_REQUEST, userId);
        return service.getAllUserEvents(userId, new PageRequestCustom(from, size, ConstantManager.SORT_EVENTS_BY_EVENT_DATE));
    }

    @GetMapping("/{eventId}")
    public EventFullDto getUserEvent(@PathVariable long userId, @PathVariable long eventId) {
        log.info(InfoMessageManager.GET_USER_EVENT_REQUEST, eventId);
        return service.getUserEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable long userId,
                                    @PathVariable long eventId,
                                    @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        log.info(InfoMessageManager.PATCH_REQUEST, eventId, updateEventUserRequest);
        return service.updateEvent(userId, eventId,updateEventUserRequest);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getAllUserEventRequests(@PathVariable long userId, @PathVariable long eventId) {
        log.info(InfoMessageManager.GET_USER_EVENT_REQUESTS, eventId);
        return service.getAllUserEventRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult approveRequestsByEvent(
            @PathVariable long userId,
            @PathVariable long eventId,
            @Valid @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.info(InfoMessageManager.GET_REQUEST_APPROVE_REQUESTS, eventId);
        return service.approveRequestsByEvent(userId, eventId, eventRequestStatusUpdateRequest);
    }
}