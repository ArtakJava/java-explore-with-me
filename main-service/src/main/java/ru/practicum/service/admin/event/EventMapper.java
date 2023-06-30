package ru.practicum.service.admin.event;

import ru.practicum.Location;
import ru.practicum.constantManager.ConstantManager;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.event.UpdateEventAdminRequest;
import ru.practicum.model.Event;

import java.time.LocalDateTime;

public class EventMapper {

    public static Event mapToEventEntity(UpdateEventAdminRequest updateEventAdminRequest) {
        return Event.builder()
                .annotation(updateEventAdminRequest.getAnnotation())
                .description(updateEventAdminRequest.getDescription())
                .eventDate(LocalDateTime.parse(updateEventAdminRequest.getEventDate(), ConstantManager.formatter))
                .lat(updateEventAdminRequest.getLocation().getLat())
                .lon(updateEventAdminRequest.getLocation().getLon())
                .paid(updateEventAdminRequest.getPaid())
                .participantLimit(updateEventAdminRequest.getParticipantLimit())
                .requestModeration(updateEventAdminRequest.getRequestModeration())
                .title(updateEventAdminRequest.getTitle())
                .build();
    }

    public static Event mapToEventEntity(NewEventDto newEventDto) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .lat(newEventDto.getLocation().getLat())
                .lon(newEventDto.getLocation().getLon())
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .title(newEventDto.getTitle())
                .build();
    }

    public static Event mapToEventEntity(EventFullDto eventFullDto) {
        return Event.builder()
                .annotation(eventFullDto.getAnnotation())
                .description(eventFullDto.getDescription())
                .eventDate(eventFullDto.getEventDate())
                .lat(eventFullDto.getLocation().getLat())
                .lon(eventFullDto.getLocation().getLon())
                .paid(eventFullDto.getPaid())
                .participantLimit(eventFullDto.getParticipantLimit())
                .requestModeration(eventFullDto.getRequestModeration())
                .title(eventFullDto.getTitle())
                .build();
    }

    public static EventFullDto mapToEventFullDto(Event event) {
        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .location(new Location(event.getLat(), event.getLon()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .requestModeration(event.getRequestModeration())
                .title(event.getTitle())
                .build();
    }
}