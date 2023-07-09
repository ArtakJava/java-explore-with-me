package ru.practicum.service.priv.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.*;
import ru.practicum.constantManager.ConstantManager;
import ru.practicum.dto.event.*;
import ru.practicum.enums.EventRequestStateUpdate;
import ru.practicum.enums.EventState;
import ru.practicum.enums.RequestState;
import ru.practicum.enums.UserStateAction;
import ru.practicum.exception.EventAlreadyPublishedException;
import ru.practicum.exception.RequestAlreadyConfirmedException;
import ru.practicum.messageManager.ErrorMessageManager;
import ru.practicum.messageManager.InfoMessageManager;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.ParticipationRequest;
import ru.practicum.model.User;
import ru.practicum.repository.*;
import ru.practicum.service.admin.event.EventMapper;
import ru.practicum.service.priv.request.RequestMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EventPrivateServiceImpl extends AbstractServiceImpl implements EventPrivateService {

    public EventPrivateServiceImpl(
            RequestRepository requestRepository,
            EventRepository eventRepository,
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            CompilationRepository compilationRepository) {
        super(requestRepository, eventRepository, userRepository, categoryRepository, compilationRepository);
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
                getViews(event, ConstantManager.DEFAULT_UNIQUE_FOR_STATS)
        );
        log.info(InfoMessageManager.SUCCESS_CREATE, result);
        return result;
    }

    @Override
    public List<EventShortDto> getAllUserEvents(long userId, PageRequestCustom pageRequestCustom) {
        User user = userRepository.getReferenceById(userId);
        List<Event> events = eventRepository.findByInitiatorId(userId, pageRequestCustom);
        List<EventShortDto> result = EventMapper.mapToEventsShortDto(
                events,
                getConfirmedRequestsByEvent(events),
                getViewsByEventId(events, ConstantManager.DEFAULT_UNIQUE_FOR_STATS)
        );
        log.info(InfoMessageManager.SUCCESS_USER_ALL_EVENTS_REQUEST, userId);
        return result;
    }

    @Override
    public EventFullDto getUserEvent(long userId, long eventId) {
        User user = userRepository.getReferenceById(userId);
        Event event = eventRepository.getReferenceById(eventId);
        EventFullDto result = EventMapper.mapToEventFullDto(
                event,
                getConfirmedRequestsCount(eventId),
                getViews(event, ConstantManager.DEFAULT_UNIQUE_FOR_STATS)
        );
        log.info(InfoMessageManager.SUCCESS_USER_EVENT_REQUEST, eventId, userId);
        return result;
    }

    @Override
    public EventFullDto patchUserEvent(long userId, long eventId, UpdateEventUserRequest updateEventUserRequest) {
        validateEventDate(updateEventUserRequest.getEventDate());
        User user = userRepository.getReferenceById(userId);
        Event oldEvent = eventRepository.getReferenceById(eventId);
        if (!oldEvent.getState().equals(EventState.PUBLISHED)) {
            Event event = eventRepository.save(getEvent(oldEvent, updateEventUserRequest));
            log.info(InfoMessageManager.SUCCESS_REQUEST_EVENT, eventId, userId);
            return EventMapper.mapToEventFullDto(
                    event,
                    getConfirmedRequestsCount(eventId),
                    getViews(event, ConstantManager.DEFAULT_UNIQUE_FOR_STATS)
            );
        } else {
            throw new EventAlreadyPublishedException(
                    String.format(ErrorMessageManager.EVENT_ALREADY_PUBLISHED, eventId)
            );
        }
    }

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

    private List<ParticipationRequestDto> getAllRequestsByIdInAndState(List<Long> requestIds, RequestState state) {
        return requestRepository.findAllByIdInAndState(requestIds, state)
                .stream()
                .map(RequestMapper::mapAprovetoParticipationRequestDto)
                .collect(Collectors.toList());
    }

    private Event getEvent(Event event, UpdateEventUserRequest updateEventUserRequest) {
        if (UserStateAction.SEND_TO_REVIEW.equals(updateEventUserRequest.getStateAction())) {
            event.setState(EventState.PENDING);
        } else if (UserStateAction.CANCEL_REVIEW.equals(updateEventUserRequest.getStateAction())) {
            event.setState(EventState.CANCELED);
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
}