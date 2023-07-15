package ru.practicum.service.priv.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.constantManager.ConstantManager;
import ru.practicum.dto.comment.*;
import ru.practicum.dto.enums.*;
import ru.practicum.dto.event.*;
import ru.practicum.dto.event.pageParameter.PageRequestCustom;
import ru.practicum.dto.request.EventRequestStatusUpdateRequest;
import ru.practicum.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.dto.event.UpdateEventUserRequest;
import ru.practicum.exception.EventAlreadyPublishedException;
import ru.practicum.exception.EventNotPublishedForRequestException;
import ru.practicum.exception.RequestAlreadyConfirmedException;
import ru.practicum.exception.UserNotAuthorCommentException;
import ru.practicum.messageManager.ErrorMessageManager;
import ru.practicum.messageManager.InfoMessageManager;
import ru.practicum.model.*;
import ru.practicum.repository.*;
import ru.practicum.service.AbstractServiceImpl;
import ru.practicum.service.admin.event.EventMapper;
import ru.practicum.service.priv.request.RequestMapper;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
@Slf4j
public class EventPrivateServiceImpl extends AbstractServiceImpl implements EventPrivateService {

    public EventPrivateServiceImpl(
            RequestRepository requestRepository,
            EventRepository eventRepository,
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            CompilationRepository compilationRepository,
            CommentRepository commentRepository) {
        super(requestRepository, eventRepository, userRepository, categoryRepository, compilationRepository, commentRepository);
    }

    @Override
    public EventFullDto create(long userId, NewEventDto newEventDto) {
        validateEventDate(newEventDto.getEventDate());
        Event event = EventMapper.mapToEventEntity(newEventDto);
        User user = userRepository.getReferenceById(userId);
        event.setInitiator(user);
        Category category = categoryRepository.getReferenceById(newEventDto.getCategory());
        event.setCategory(category);
        EventFullDto result = EventMapper.mapToEventFullDto(
                eventRepository.save(event),
                getConfirmedRequestsCount(event.getId()),
                getViews(event, ConstantManager.DEFAULT_UNIQUE_FOR_STATS),
                getCommentsByEvent(event.getId())
        );
        log.info(InfoMessageManager.SUCCESS_CREATE, result);
        return result;
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventShortDto> getAllUserEvents(long userId, PageRequestCustom pageRequestCustom) {
        User user = userRepository.getReferenceById(userId);
        List<Event> events = eventRepository.findByInitiatorId(userId, pageRequestCustom);

        List<EventShortDto> result = EventMapper.mapToEventsShortDto(
                events,
                getConfirmedRequestsCountByEvent(events),
                getViewsByEventId(events, ConstantManager.DEFAULT_UNIQUE_FOR_STATS),
                getPublishedCommentsCountByEvent(events)
        );
        log.info(InfoMessageManager.SUCCESS_USER_ALL_EVENTS_REQUEST, userId);
        return result;
    }

    @Transactional(readOnly = true)
    @Override
    public EventFullDto getUserEvent(long userId, long eventId) {
        User user = userRepository.getReferenceById(userId);
        Event event = eventRepository.getReferenceById(eventId);
        EventFullDto result = EventMapper.mapToEventFullDto(
                event,
                getConfirmedRequestsCount(eventId),
                getViews(event, ConstantManager.DEFAULT_UNIQUE_FOR_STATS),
                getCommentsByEvent(eventId)
        );
        log.info(InfoMessageManager.SUCCESS_USER_EVENT_REQUEST, eventId, userId);
        return result;
    }

    @Override
    public EventFullDto updateEvent(long userId, long eventId, UpdateEventUserRequest updateEventUserRequest) {
        validateEventDate(updateEventUserRequest.getEventDate());
        User user = userRepository.getReferenceById(userId);
        Event oldEvent = eventRepository.getReferenceById(eventId);
        if (!oldEvent.getState().equals(ModerationState.PUBLISHED)) {
            Event event = eventRepository.save(getEvent(oldEvent, updateEventUserRequest));
            log.info(InfoMessageManager.SUCCESS_REQUEST_EVENT, eventId, userId);
            return EventMapper.mapToEventFullDto(
                    event,
                    getConfirmedRequestsCount(eventId),
                    getViews(event, ConstantManager.DEFAULT_UNIQUE_FOR_STATS),
                    getCommentsByEvent(eventId)
            );
        } else {
            throw new EventAlreadyPublishedException(
                    String.format(ErrorMessageManager.EVENT_ALREADY_PUBLISHED, eventId)
            );
        }
    }

    @Transactional(readOnly = true)
    @Override
    public List<ParticipationRequestDto> getAllUserEventRequests(long userId, long eventId) {
        User user = userRepository.getReferenceById(userId);
        Event oldEvent = eventRepository.getReferenceById(eventId);
        List<ParticipationRequest> requests = requestRepository.findAllByEventId(eventId);
        log.info(InfoMessageManager.SUCCESS_USER_EVENT_REQUESTS, eventId);
        return RequestMapper.mapToParticipationRequestsDto(requests);
    }

    @Override
    public EventRequestStatusUpdateResult approveRequestsByEvent(long userId,
                                                                 long eventId,
                                                                 EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        User user = userRepository.getReferenceById(userId);
        Event event = eventRepository.getReferenceById(eventId);
        validateParticipantLimit(event);
        List<Long> requestIds = eventRequestStatusUpdateRequest.getRequestIds();
        List<ParticipationRequest> requests = requestRepository.findAllByIdIn(requestIds);
        requests.forEach(request ->
                setRequestState(request, getRequestState(eventRequestStatusUpdateRequest.getStatus()))
        );
        requestRepository.saveAll(requests);
        log.info(InfoMessageManager.SUCCESS_REQUEST_APPROVE_REQUESTS, eventId);
        List<ParticipationRequestDto> confirmedRequests = getAllRequestsByIdInAndState(requestIds, RequestState.CONFIRMED);
        List<ParticipationRequestDto> rejectedRequests = getAllRequestsByIdInAndState(requestIds, RequestState.CANCELED);
        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmedRequests)
                .rejectedRequests(rejectedRequests)
                .build();
    }

    @Override
    public CommentFullDto createComment(long userId, long eventId, NewCommentDto newCommentDto) {
        Event event = eventRepository.getReferenceById(eventId);
        if (event.getState().equals(ModerationState.PUBLISHED)) {
            User user = userRepository.getReferenceById(userId);
            Comment comment = CommentMapper.mapToCommentEntity(user, event, newCommentDto);
            CommentFullDto result = CommentMapper.mapToCommentFullDto(commentRepository.save(comment));
            log.info(InfoMessageManager.SUCCESS_REQUEST_COMMENT, newCommentDto, userId, eventId);
            return result;
        } else {
            throw new EventNotPublishedForRequestException(
                    String.format(ErrorMessageManager.EVENT_NOT_PUBLISHED, eventId)
            );
        }
    }

    @Override
    public CommentFullDto updateComment(long userId, long commentId, UpdateCommentUserRequest updateCommentUserRequest) {
        User user = userRepository.getReferenceById(userId);
        Comment oldComment = commentRepository.getReferenceById(commentId);
        if (oldComment.getAuthor().getId() != userId) {
            throw new UserNotAuthorCommentException(
                    String.format(ErrorMessageManager.USER_NOT_AUTHOR, userId, commentId)
            );
        }
        Comment comment = commentRepository.save(getComment(oldComment, updateCommentUserRequest));
        CommentFullDto resultDto = CommentMapper.mapToCommentFullDto(comment);
        log.info(InfoMessageManager.SUCCESS_PATCH, commentId, updateCommentUserRequest);
        return resultDto;
    }

    @Override
    public void deleteComment(long userId, long commentId) {
        User user = userRepository.getReferenceById(userId);
        Comment comment = commentRepository.getReferenceById(commentId);
        commentRepository.delete(comment);
        log.info(InfoMessageManager.SUCCESS_DELETE, comment);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentShortDto> getAllComments(long userId, PageRequestCustom pageRequestCustom) {
        User user = userRepository.getReferenceById(userId);
        List<Comment> comments = commentRepository.findAllByAuthorId(userId, pageRequestCustom);
        log.info(InfoMessageManager.SUCCESS_USER_EVENT_COMMENT, userId);
        return comments.stream()
                .map(CommentMapper::mapToCommentFullDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    private List<ParticipationRequestDto> getAllRequestsByIdInAndState(List<Long> requestIds, RequestState state) {
        return requestRepository.findAllByIdInAndState(requestIds, state)
                .stream()
                .map(RequestMapper::mapAprovetoParticipationRequestDto)
                .collect(Collectors.toList());
    }

    private Event getEvent(Event event, UpdateEventUserRequest updateEventUserRequest) {
        if (UserStateAction.SEND_TO_REVIEW.equals(updateEventUserRequest.getStateAction())) {
            event.setState(ModerationState.PENDING);
        } else if (UserStateAction.CANCEL_REVIEW.equals(updateEventUserRequest.getStateAction())) {
            event.setState(ModerationState.CANCELED);
        }
        return getUpdatedEvent(event, updateEventUserRequest);
    }

    private RequestState getRequestState(EventRequestStateUpdate status) {
        RequestState state = RequestState.PENDING;
        switch (status) {
            case CONFIRMED:
                state = RequestState.CONFIRMED;
                break;
            case REJECTED:
                state = RequestState.CANCELED;
                break;
            default:
                break;
        }
        return state;
    }

    private void setRequestState(ParticipationRequest request, RequestState requestState) {
        if (request.getState().equals(RequestState.CONFIRMED) && RequestState.CANCELED.equals(requestState)) {
            throw new RequestAlreadyConfirmedException(
                    String.format(ErrorMessageManager.REQUEST_ALREADY_CONFIRMED, request.getId())
            );
        } else {
            request.setState(requestState);
        }
    }

    private Comment getComment(Comment comment, UpdateCommentUserRequest updateCommentUserRequest) {
        if (UserStateAction.SEND_TO_REVIEW.equals(updateCommentUserRequest.getStateAction())) {
            comment.setState(ModerationState.PENDING);
        } else if (UserStateAction.CANCEL_REVIEW.equals(updateCommentUserRequest.getStateAction())) {
            comment.setState(ModerationState.CANCELED);
        }
        return getUpdatedComment(comment, updateCommentUserRequest);
    }
}