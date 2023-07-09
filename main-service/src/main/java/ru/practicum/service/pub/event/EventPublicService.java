package ru.practicum.service.pub.event;

import ru.practicum.AbstractService;
import ru.practicum.EventPublicPageParameter;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventPublicService extends AbstractService {

    List<EventShortDto> getEvents(EventPublicPageParameter eventPublicPageParameter);

    EventFullDto getById(long eventId, HttpServletRequest request);
}