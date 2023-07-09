package ru.practicum.controller.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.ParticipationRequestDto;
import ru.practicum.messageManager.InfoMessageManager;
import ru.practicum.service.priv.request.RequestPrivateService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@Slf4j
@RequiredArgsConstructor
public class RequestPrivateController {
    private final RequestPrivateService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createRequest(@PathVariable long userId, @RequestParam long eventId) {
        log.info(InfoMessageManager.POST_REQUEST_IN_EVENT, eventId);
        return service.createRequest(userId, eventId);
    }

    @GetMapping
    public List<ParticipationRequestDto> getAllUserEventRequests(@PathVariable long userId) {
        log.info(InfoMessageManager.GET_USER_ALL_REQUESTS_IN_OTHERS_EVENTS, userId);
        return service.getAllUserEventRequests(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable long userId, @PathVariable long requestId) {
        log.info(InfoMessageManager.GET_CANCEL_REQUESTS, requestId, userId);
        return service.cancelRequest(userId, requestId);
    }
}