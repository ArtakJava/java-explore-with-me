package ru.practicum.service.admin.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.*;
import ru.practicum.constantManager.ConstantManager;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.exception.EventAlreadyPublishedException;
import ru.practicum.messageManager.ErrorMessageManager;
import ru.practicum.messageManager.InfoMessageManager;
import ru.practicum.model.Event;
import ru.practicum.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EventAdminServiceImpl extends AbstractServiceImpl implements EventAdminService {

    public EventAdminServiceImpl(
            RequestRepository requestRepository,
            EventRepository eventRepository,
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            CompilationRepository compilationRepository) {
        super(requestRepository, eventRepository, userRepository, categoryRepository, compilationRepository);
    }

    @Override
    public EventFullDto update(long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        validateEventDate(updateEventAdminRequest.getEventDate());
        Event oldEvent = eventRepository.getReferenceById(eventId);
        Event event = eventRepository.save(getEvent(oldEvent, updateEventAdminRequest));
        EventFullDto resultDto = EventMapper.mapToEventFullDto(
                event,
                getConfirmedRequestsCount(event.getId()),
                getViews(event, ConstantManager.DEFAULT_UNIQUE_FOR_STATS)
        );
        log.info(InfoMessageManager.SUCCESS_PATCH, eventId, updateEventAdminRequest);
        return resultDto;
    }

    @Override
    public List<EventFullDto> getEvents(EventAdminPageParameter eventAdminPageParameter) {
        List<Event> events = new ArrayList<>();
        PageParameterCode code = eventAdminPageParameter.getPageParameterCode();
        switch (code) {
            case WITH_ALL_PARAMETERS:
                events = eventRepository.findAllByInitiatorIdInAndStateInAndCategoryIdInAndEventDateAfterAndEventDateBefore(
                        eventAdminPageParameter.getUsers(),
                        Arrays.stream(eventAdminPageParameter.getStates()).map(EventState::valueOf).collect(Collectors.toList()),
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
                        Arrays.stream(eventAdminPageParameter.getStates()).map(EventState::valueOf).collect(Collectors.toList()),
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
                getConfirmedRequestsByEvent(events),
                getViewsByEventId(events, ConstantManager.DEFAULT_UNIQUE_FOR_STATS)
        );
        log.info(InfoMessageManager.SUCCESS_GET_ALL_EVENTS);
        return result;
    }

    private Event getEvent(Event event, UpdateEventAdminRequest updateEventAdminRequest) {
        if (AdminStateAction.PUBLISH_EVENT.equals(updateEventAdminRequest.getStateAction())) {
            if (event.getState().equals(EventState.PENDING)) {
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            } else {
                throw new EventAlreadyPublishedException(
                        String.format(ErrorMessageManager.EVENT_ALREADY_PUBLISHED, event.getId())
                );
            }
        } else if (AdminStateAction.REJECT_EVENT.equals(updateEventAdminRequest.getStateAction())) {
            if (!event.getState().equals(EventState.PUBLISHED)) {
                event.setState(EventState.CANCELED);
            } else {
                throw new EventAlreadyPublishedException(
                        String.format(ErrorMessageManager.EVENT_ALREADY_PUBLISHED, event.getId())
                );
            }
            event.setState(EventState.CANCELED);
        }
        return getUpdatedEvent(event, updateEventAdminRequest);
    }
}