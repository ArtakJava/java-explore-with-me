package ru.practicum.service.admin.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.AdminStateAction;
import ru.practicum.EventPageParameter;
import ru.practicum.EventState;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.messageManager.InfoMessageManager;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventAdminServiceImpl implements EventAdminService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public EventFullDto update(long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event oldEvent = eventRepository.getReferenceById(eventId);
        Event eventPatch = EventMapper.mapToEventEntity(updateEventAdminRequest);
        if (updateEventAdminRequest.getCategory() != null) {
            Category category = categoryRepository.getReferenceById(updateEventAdminRequest.getCategory());
            eventPatch.setCategory(category);
        }
        if (updateEventAdminRequest.getStateAction().equals(AdminStateAction.PUBLISH_EVENT)) {
            eventPatch.setState(EventState.PUBLISHED);
        } else if (updateEventAdminRequest.getStateAction().equals(AdminStateAction.REJECT_EVENT)) {
            eventPatch.setState(EventState.CANCELED);
        }
        Event result = eventRepository.save(getUpdatedEvent(oldEvent, eventPatch));
        EventFullDto resultDto = EventMapper.mapToEventFullDto(result);
        log.info(InfoMessageManager.SUCCESS_PATCH, eventId, updateEventAdminRequest);
        return resultDto;
    }

    @Override
    public List<EventFullDto> getEvents(EventPageParameter eventPageParameter) {
        List<Event> result = eventRepository.findAll();
        return result.stream()
                .map(EventMapper::mapToEventFullDto)
                .collect(Collectors.toList());
    }

    public Event getUpdatedEvent(Event event, Event eventPatch) {
        if (eventPatch.getAnnotation() != null) {
            event.setAnnotation(eventPatch.getAnnotation());
        }if (eventPatch.getCategory() != null) {
            event.setCategory(eventPatch.getCategory());
        }
        if (eventPatch.getDescription() != null) {
            event.setDescription(eventPatch.getDescription());
        }
        if (eventPatch.getEventDate() != null) {
            event.setEventDate(eventPatch.getEventDate());
        }
        if (eventPatch.getLat() != null && eventPatch.getLon() != null) {
            event.setLat(eventPatch.getLat());
            event.setLon(eventPatch.getLon());
        }
        if (eventPatch.getPaid() != null) {
            event.setPaid(eventPatch.getPaid());
        }
        if (eventPatch.getParticipantLimit() != null) {
            event.setParticipantLimit(eventPatch.getParticipantLimit());
        }
        if (eventPatch.getRequestModeration() != null) {
            event.setRequestModeration(eventPatch.getRequestModeration());
        }
        if (eventPatch.getTitle() != null) {
            event.setTitle(eventPatch.getTitle());
        }
        return event;
    }
}