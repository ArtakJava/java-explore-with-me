package ru.practicum.service.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.PageRequestCustom;
import ru.practicum.dto.event.*;
import ru.practicum.dto.user.UserDto;
import ru.practicum.messageManager.InfoMessageManager;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.User;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.admin.event.EventMapper;
import ru.practicum.service.admin.user.UserMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventPrivateServiceImpl implements EventPrivateService {
    private EventRepository eventRepository;
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;

    @Override
    public EventFullDto create(long userId, NewEventDto newEventDto) {
        Event event = EventMapper.mapToEventEntity(newEventDto);
        User user = userRepository.getReferenceById(userId);
        event.setInitiator(user);
        Category category = categoryRepository.getReferenceById(newEventDto.getCategory());
        event.setCategory(category);
        EventFullDto result = EventMapper.mapToEventFullDto(eventRepository.save(event));
        log.info(InfoMessageManager.SUCCESS_CREATE, result);
        return result;
    }

    @Override
    public List<EventShortDto> getAllUserEvents(long userId, PageRequestCustom pageRequestCustom) {
        return null;
    }

    @Override
    public EventFullDto getUserEvent(long userId, long eventId) {
        return null;
    }

    @Override
    public EventFullDto patchUserEvent(long userId, long eventId, UpdateEventUserRequest updateEventUserRequest) {
        return null;
    }

    @Override
    public List<ParticipationRequestDto> getAllUserEventRequests(long userId, long eventId) {
        return null;
    }

    @Override
    public EventRequestStatusUpdateResult approveRequestsByEvent(long userId, long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        return null;
    }
}