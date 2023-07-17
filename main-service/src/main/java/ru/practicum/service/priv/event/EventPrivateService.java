package ru.practicum.service.priv.event;

import ru.practicum.dto.comment.*;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.dto.event.pageParameter.PageRequestCustom;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.service.AbstractService;

import java.util.List;

public interface EventPrivateService extends AbstractService {

    EventFullDto create(long userId, NewEventDto newEventDto);

    List<EventShortDto> getAllUserEvents(long userId, PageRequestCustom pageRequestCustom);

    EventFullDto getUserEvent(long userId, long eventId);

    EventFullDto updateEvent(long userId, long eventId, UpdateEventUserRequest updateEventUserRequest);

    List<ParticipationRequestDto> getAllUserEventRequests(long userId, long eventId);

    EventRequestStatusUpdateResult approveRequestsByEvent(long userId, long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);

    CommentFullDto createComment(long userId, long eventId, NewCommentDto newCommentDto);

    CommentFullDto updateComment(long userId, long commentId, UpdateCommentUserRequest updateCommentUserRequest);

    void deleteComment(long userId, long commentId);

    List<CommentShortDto> getAllComments(long userId, PageRequestCustom pageRequestCustom);
}