package ru.practicum.service;

import ru.practicum.dto.request.UpdateEventRequest;
import ru.practicum.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface AbstractService {

    void validateEventDate(LocalDateTime eventDate);

    long getConfirmedRequestsCount(long eventId);

    Map<Long, Integer> getConfirmedRequestsByEvent(List<Event> events);

    Map<Long, Long> getViewsByEventId(List<Event> events, boolean unique);

    long getViews(Event event, boolean unique);

    void validateParticipantLimit(Event event);

    Event getUpdatedEvent(Event event, UpdateEventRequest updateEventRequest);
}