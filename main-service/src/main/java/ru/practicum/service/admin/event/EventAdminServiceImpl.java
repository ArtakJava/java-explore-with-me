package ru.practicum.service.admin.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.constantManager.ConstantManager;
import ru.practicum.dto.comment.CommentFullDto;
import ru.practicum.dto.comment.UpdateCommentAdminRequest;
import ru.practicum.dto.enums.AdminCommentStateAction;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.pageParameter.EventAdminPageParameter;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.dto.enums.AdminEventStateAction;
import ru.practicum.dto.enums.ModerationState;
import ru.practicum.dto.enums.PageParameterCode;
import ru.practicum.dto.event.pageParameter.PageRequestCustom;
import ru.practicum.exception.CommentAlreadyPublishedOrRejectedException;
import ru.practicum.exception.EventAlreadyPublishedException;
import ru.practicum.messageManager.ErrorMessageManager;
import ru.practicum.messageManager.InfoMessageManager;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.repository.*;
import ru.practicum.service.AbstractServiceImpl;
import ru.practicum.service.priv.event.CommentMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@Slf4j
public class EventAdminServiceImpl extends AbstractServiceImpl implements EventAdminService {

    public EventAdminServiceImpl(
            RequestRepository requestRepository,
            EventRepository eventRepository,
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            CompilationRepository compilationRepository,
            CommentRepository commentRepository) {
        super(requestRepository, eventRepository, userRepository, categoryRepository, compilationRepository, commentRepository);
    }

    @Override
    public EventFullDto updateEvent(long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        validateEventDate(updateEventAdminRequest.getEventDate());
        Event oldEvent = eventRepository.getReferenceById(eventId);
        Event event = eventRepository.save(getEvent(oldEvent, updateEventAdminRequest));
        EventFullDto resultDto = EventMapper.mapToEventFullDto(
                event,
                getConfirmedRequestsCount(event.getId()),
                getViews(event, ConstantManager.DEFAULT_UNIQUE_FOR_STATS),
                getCommentsByEvent(event.getId())
        );
        log.info(InfoMessageManager.SUCCESS_PATCH, eventId, updateEventAdminRequest);
        return resultDto;
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventFullDto> getEvents(EventAdminPageParameter eventAdminPageParameter) {
        List<Event> events = new ArrayList<>();
        PageParameterCode code = eventAdminPageParameter.getPageParameterCode();
        switch (code) {
            case WITH_ALL_PARAMETERS:
                events = eventRepository.findAllByInitiatorIdInAndStateInAndCategoryIdInAndEventDateAfterAndEventDateBefore(
                        eventAdminPageParameter.getUsers(),
                        Arrays.stream(eventAdminPageParameter.getStates()).map(ModerationState::valueOf).collect(Collectors.toList()),
                        eventAdminPageParameter.getCategories(),
                        eventAdminPageParameter.getRangeStart(),
                        eventAdminPageParameter.getRangeEnd(),
                        eventAdminPageParameter.getPageRequest()
                );
                break;
            case WITHOUT_PARAMETERS:
                events = eventRepository.findAllByEventDateAfter(
                        LocalDateTime.now(),
                        eventAdminPageParameter.getPageRequest()
                );
                break;
            case WITHOUT_DATES:
                events = eventRepository.findAllByInitiatorIdInAndStateInAndCategoryIdInAndEventDateAfter(
                        eventAdminPageParameter.getUsers(),
                        Arrays.stream(eventAdminPageParameter.getStates()).map(ModerationState::valueOf).collect(Collectors.toList()),
                        eventAdminPageParameter.getCategories(),
                        LocalDateTime.now(),
                        eventAdminPageParameter.getPageRequest()
                );
                break;
            case ONLY_CATEGORY:
                events = eventRepository.findAllByCategoryIdInAndEventDateAfter(
                        eventAdminPageParameter.getCategories(),
                        LocalDateTime.now(),
                        eventAdminPageParameter.getPageRequest()
                );
                break;
            case USERS_AND_CATEGORIES:
                events = eventRepository.findAllByInitiatorIdInAndCategoryIdInAndEventDateAfter(
                        eventAdminPageParameter.getUsers(),
                        eventAdminPageParameter.getCategories(),
                        LocalDateTime.now(),
                        eventAdminPageParameter.getPageRequest()
                );
                break;
        }
        List<EventFullDto> result = EventMapper.mapToEventsFullDto(
                events,
                getConfirmedRequestsCountByEvent(events),
                getViewsByEventId(events, ConstantManager.DEFAULT_UNIQUE_FOR_STATS),
                getCommentsByEvents(events)
        );
        log.info(InfoMessageManager.SUCCESS_GET_ALL_EVENTS);
        return result;
    }

    @Override
    public CommentFullDto updateComment(long commentId, UpdateCommentAdminRequest updateCommentAdminRequest) {
        Comment oldComment = commentRepository.getReferenceById(commentId);
        Comment comment = commentRepository.save(getComment(oldComment, updateCommentAdminRequest));
        CommentFullDto resultDto = CommentMapper.mapToCommentFullDto(comment);
        log.info(InfoMessageManager.SUCCESS_PATCH, commentId, updateCommentAdminRequest);
        return resultDto;
    }

    @Override
    public List<CommentFullDto> getComments(String[] states, PageRequestCustom pageRequest) {
        List<Comment> comments;
        if (states != null) {
            comments = commentRepository.findAllByStateIn(
                    Arrays.stream(states).map(ModerationState::valueOf).collect(Collectors.toList()),
                    pageRequest
            );
        } else {
            comments = commentRepository.findAll(pageRequest).toList();
        }
        List<CommentFullDto> result = comments.stream()
                .map(CommentMapper::mapToCommentFullDto)
                .collect(Collectors.toList());
        log.info(InfoMessageManager.SUCCESS_GET_ALL_COMMENTS);
        return result;
    }

    private Comment getComment(Comment comment, UpdateCommentAdminRequest updateCommentAdminRequest) {
        if (AdminCommentStateAction.PUBLISH_COMMENT.equals(updateCommentAdminRequest.getStateAction())) {
            if (comment.getState().equals(ModerationState.PENDING)) {
                comment.setState(ModerationState.PUBLISHED);
                comment.setPublishedOn(LocalDateTime.now());
            } else {
                throw new CommentAlreadyPublishedOrRejectedException(
                        String.format(ErrorMessageManager.COMMENT_ALREADY_PUBLISHED, comment.getId())
                );
            }
        } else if (AdminCommentStateAction.REJECT_COMMENT.equals(updateCommentAdminRequest.getStateAction())) {
            if (!comment.getState().equals(ModerationState.PUBLISHED)) {
                comment.setState(ModerationState.CANCELED);
            } else {
                throw new CommentAlreadyPublishedOrRejectedException(
                        String.format(ErrorMessageManager.COMMENT_ALREADY_PUBLISHED, comment.getId())
                );
            }
            comment.setState(ModerationState.CANCELED);
        }
        return getUpdatedComment(comment, updateCommentAdminRequest);
    }

    private Event getEvent(Event event, UpdateEventAdminRequest updateEventAdminRequest) {
        if (AdminEventStateAction.PUBLISH_EVENT.equals(updateEventAdminRequest.getStateAction())) {
            if (event.getState().equals(ModerationState.PENDING)) {
                event.setState(ModerationState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            } else {
                throw new EventAlreadyPublishedException(
                        String.format(ErrorMessageManager.EVENT_ALREADY_PUBLISHED, event.getId())
                );
            }
        } else if (AdminEventStateAction.REJECT_EVENT.equals(updateEventAdminRequest.getStateAction())) {
            if (!event.getState().equals(ModerationState.PUBLISHED)) {
                event.setState(ModerationState.CANCELED);
            } else {
                throw new EventAlreadyPublishedException(
                        String.format(ErrorMessageManager.EVENT_ALREADY_PUBLISHED, event.getId())
                );
            }
            event.setState(ModerationState.CANCELED);
        }
        return getUpdatedEvent(event, updateEventAdminRequest);
    }
}