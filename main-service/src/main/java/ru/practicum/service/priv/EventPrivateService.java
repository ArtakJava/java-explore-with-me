package ru.practicum.service.priv;

import ru.practicum.PageRequestCustom;
import ru.practicum.dto.event.*;

import java.util.List;

public interface EventPrivateService {

    EventFullDto create(long userId, NewEventDto newEventDto);

    List<EventShortDto> getAllUserEvents(long userId, PageRequestCustom pageRequestCustom);

    EventFullDto getUserEvent(long userId, long eventId);

    EventFullDto patchUserEvent(long userId, long eventId, UpdateEventUserRequest updateEventUserRequest);

    List<ParticipationRequestDto> getAllUserEventRequests(long userId, long eventId);

    EventRequestStatusUpdateResult approveRequestsByEvent(long userId, long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);
}