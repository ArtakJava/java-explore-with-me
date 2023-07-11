package ru.practicum.service.admin.event;

import ru.practicum.service.AbstractService;
import ru.practicum.dto.event.pageParameter.EventAdminPageParameter;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.request.UpdateEventAdminRequest;

import java.util.List;

public interface EventAdminService extends AbstractService {

    EventFullDto update(long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    List<EventFullDto> getEvents(EventAdminPageParameter eventAdminPageParameter);
}