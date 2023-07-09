package ru.practicum.service.priv.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.AbstractServiceImpl;
import ru.practicum.enums.EventState;
import ru.practicum.enums.RequestState;
import ru.practicum.dto.event.ParticipationRequestDto;
import ru.practicum.exception.EventNotPublishedForRequestException;
import ru.practicum.exception.EventParticipantOwnerException;
import ru.practicum.exception.UserAlreadySendRequestException;
import ru.practicum.messageManager.ErrorMessageManager;
import ru.practicum.messageManager.InfoMessageManager;
import ru.practicum.model.Event;
import ru.practicum.model.ParticipationRequest;
import ru.practicum.model.User;
import ru.practicum.repository.*;

import java.util.List;

@Service
@Slf4j
public class RequestPrivateServiceImpl extends AbstractServiceImpl implements RequestPrivateService {

    public RequestPrivateServiceImpl(
            RequestRepository requestRepository,
            EventRepository eventRepository,
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            CompilationRepository compilationRepository) {
        super(requestRepository, eventRepository, userRepository, categoryRepository, compilationRepository);
    }

    @Override
    public ParticipationRequestDto createRequest(long userId, long eventId) {
        User user = userRepository.getReferenceById(userId);
        Event event = eventRepository.getReferenceById(eventId);
        if (event.getState().equals(EventState.PUBLISHED)) {
            validateRequesterIdInEvent(userId, event);
            ParticipationRequest request = RequestMapper.mapToRequestEntity(user, event);
            if (event.getParticipantLimit() != 0) {
                validateParticipantLimit(event);
            }
            if (event.getRequestModeration().equals(false) || event.getParticipantLimit() == 0) {
                request.setState(RequestState.CONFIRMED);
            }
            ParticipationRequest participationRequest = requestRepository.save(request);
            log.info(InfoMessageManager.SUCCESS_REQUEST_IN_EVENT, eventId);
            return RequestMapper.maptoParticipationRequestDto(participationRequest);
        } else {
            throw new EventNotPublishedForRequestException(
                    String.format(ErrorMessageManager.EVENT_NOT_PUBLISHED, eventId)
            );
        }
    }

    @Override
    public List<ParticipationRequestDto> getAllUserEventRequests(long userId) {
        User user = userRepository.getReferenceById(userId);
        List<ParticipationRequest> participationRequests = requestRepository.findAllByRequesterId(userId);
        log.info(InfoMessageManager.SUCCESS_USER_ALL_REQUESTS_IN_OTHERS_EVENTS, userId);
        return RequestMapper.mapToParticipationRequestsDto(participationRequests);
    }

    @Override
    public ParticipationRequestDto cancelRequest(long userId, long requestId) {
        User user = userRepository.getReferenceById(userId);
        ParticipationRequest participationRequest = requestRepository.getReferenceById(requestId);
        participationRequest.setState(RequestState.CANCELED);
        ParticipationRequestDto result =
                RequestMapper.maptoParticipationRequestDto(requestRepository.save(participationRequest));
        log.info(InfoMessageManager.SUCCESS_CANCEL_REQUESTS, requestId, userId);
        return result;
    }

    private void validateRequesterIdInEvent(long userId, Event event) {
        List<Long> requesterIdsInEvent = requestRepository.findAllRequesterIdsInEvent(event.getId());
        if (event.getInitiator().getId() == userId) {
            throw new EventParticipantOwnerException(
                    String.format(ErrorMessageManager.EVENT_PARTICIPANT_OWNER, event.getId(), userId)
            );
        }
        if (requesterIdsInEvent.contains(userId)) {
            throw new UserAlreadySendRequestException(
                    String.format(ErrorMessageManager.USER_ALREADY_SEND_REQUEST, userId, event.getId())
            );
        }
    }
}