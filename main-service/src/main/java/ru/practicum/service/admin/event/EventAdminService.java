package ru.practicum.service.admin.event;

import ru.practicum.EventPageParameter;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.UpdateEventAdminRequest;

import java.util.List;

public interface EventAdminService {

    EventFullDto update(long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    List<EventFullDto> getEvents(EventPageParameter eventPageParameter);
}