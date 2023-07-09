package ru.practicum.service.priv.event;

import ru.practicum.service.AbstractService;
import ru.practicum.PageRequestCustom;
import ru.practicum.dto.event.*;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.dto.request.UpdateEventUserRequest;

import java.util.List;

public interface EventPrivateService extends AbstractService {

    EventFullDto create(long userId, NewEventDto newEventDto);

    List<EventShortDto> getAllUserEvents(long userId, PageRequestCustom pageRequestCustom);

    EventFullDto getUserEvent(long userId, long eventId);

    EventFullDto patchUserEvent(long userId, long eventId, UpdateEventUserRequest updateEventUserRequest);

    List<ParticipationRequestDto> getAllUserEventRequests(long userId, long eventId);

    EventRequestStatusUpdateResult approveRequestsByEvent(long userId, long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);
}