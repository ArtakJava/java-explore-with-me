package ru.practicum.service.admin.event;

import ru.practicum.dto.comment.CommentFullDto;
import ru.practicum.dto.comment.UpdateCommentAdminRequest;
import ru.practicum.dto.event.pageParameter.PageRequestCustom;
import ru.practicum.service.AbstractService;
import ru.practicum.dto.event.pageParameter.EventAdminPageParameter;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventAdminRequest;

import java.util.List;

public interface EventAdminService extends AbstractService {

    EventFullDto updateEvent(long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    List<EventFullDto> getEvents(EventAdminPageParameter eventAdminPageParameter);

    CommentFullDto updateComment(long commentId, UpdateCommentAdminRequest updateCommentAdminRequest);

    List<CommentFullDto> getComments(String[] states, PageRequestCustom pageRequestCustom);
}