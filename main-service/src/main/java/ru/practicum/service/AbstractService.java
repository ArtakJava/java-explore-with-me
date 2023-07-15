package ru.practicum.service;

import ru.practicum.dto.comment.UpdateCommentRequest;
import ru.practicum.dto.event.UpdateEventRequest;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface AbstractService {

    void validateEventDate(LocalDateTime eventDate);

    long getConfirmedRequestsCount(long eventId);

    Map<Long, Integer> getConfirmedRequestsCountByEvent(List<Event> events);

    Map<Long, Integer> getPublishedCommentsCountByEvent(List<Event> events);

    Map<Long, Long> getViewsByEventId(List<Event> events, boolean unique);

    long getViews(Event event, boolean unique);

    void validateParticipantLimit(Event event);

    Event getUpdatedEvent(Event event, UpdateEventRequest updateEventRequest);

    Comment getUpdatedComment(Comment comment, UpdateCommentRequest updateCommentRequest);

    List<Comment> getCommentsByEvent(long eventId);

    Map<Long, List<Comment>> getCommentsByEvents(List<Event> events);
}