package ru.practicum.service.admin.event;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.enums.EventState;
import ru.practicum.dto.Location;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.dto.request.UpdateEventAdminRequest;
import ru.practicum.model.Event;
import ru.practicum.service.admin.category.CategoryMapper;
import ru.practicum.service.admin.user.UserMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class EventMapper {

    public static Event mapToEventEntity(UpdateEventAdminRequest updateEventAdminRequest) {
        return Event.builder()
                .annotation(updateEventAdminRequest.getAnnotation())
                .description(updateEventAdminRequest.getDescription())
                .eventDate(updateEventAdminRequest.getEventDate())
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
                .createdOn(LocalDateTime.now())
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .lat(newEventDto.getLocation().getLat())
                .lon(newEventDto.getLocation().getLon())
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .state(EventState.PENDING)
                .title(newEventDto.getTitle())
                .build();
    }

    public static EventFullDto mapToEventFullDto(Event event, long confirmedRequests, long views) {
        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.mapToCategoryDto(event.getCategory()))
                .confirmedRequests(confirmedRequests)
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserMapper.mapToUserShortDto(event.getInitiator()))
                .location(new Location(event.getLat(), event.getLon()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(views)
                .build();
    }

    public static EventShortDto mapToEventShortDto(Event event, long confirmedRequests, long views) {
        return EventShortDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.mapToCategoryDto(event.getCategory()))
                .confirmedRequests(confirmedRequests)
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserMapper.mapToUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(views)
                .build();
    }

    public static List<EventShortDto> mapToEventsShortDto(List<Event> events,
                                                          Map<Long, Integer> requests,
                                                          Map<Long, Long> views) {
        return events.stream()
                .map(event -> EventMapper.mapToEventShortDto(
                        event,
                        requests.getOrDefault(event.getId(), 0),
                        views.getOrDefault(event.getId(), 0L)
                        )
                )
                .collect(Collectors.toList());
    }

    public static List<EventFullDto> mapToEventsFullDto(List<Event> events,
                                                        Map<Long, Integer> requests,
                                                        Map<Long, Long> views) {
        return events.stream()
                .map(event -> EventMapper.mapToEventFullDto(
                                event,
                                requests.getOrDefault(event.getId(), 0),
                                views.getOrDefault(event.getId(), 0L)
                        )
                )
                .collect(Collectors.toList());
    }
}