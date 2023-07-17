package ru.practicum.service.admin.event;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.Location;
import ru.practicum.dto.enums.ModerationState;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.NewEventDto;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.service.admin.category.CategoryMapper;
import ru.practicum.service.admin.user.UserMapper;
import ru.practicum.service.priv.event.CommentMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class EventMapper {

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
                .state(ModerationState.PENDING)
                .title(newEventDto.getTitle())
                .build();
    }

    public static EventFullDto mapToEventFullDto(Event event,
                                                 long confirmedRequests,
                                                 long views,
                                                 List<Comment> comments) {
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
                .createdOn(event.getCreatedOn())
                .comments(
                        comments.stream()
                                .map(CommentMapper::mapToCommentShortDto)
                                .collect(Collectors.toList()))
                .build();
    }

    public static EventShortDto mapToEventShortDto(Event event, long confirmedRequests, long views, long comments) {
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
                .comments(comments)
                .build();
    }

    public static List<EventShortDto> mapToEventsShortDto(List<Event> events,
                                                          Map<Long, Integer> requests,
                                                          Map<Long, Long> views,
                                                          Map<Long, Integer> comments) {
        return events.stream()
                .map(event -> EventMapper.mapToEventShortDto(
                        event,
                        requests.getOrDefault(event.getId(), 0),
                        views.getOrDefault(event.getId(), 0L),
                        comments.getOrDefault(event.getId(), 0)
                        )
                )
                .collect(Collectors.toList());
    }

    public static List<EventFullDto> mapToEventsFullDto(List<Event> events,
                                                        Map<Long, Integer> requests,
                                                        Map<Long, Long> views,
                                                        Map<Long, List<Comment>> comments) {
        return events.stream()
                .map(event -> EventMapper.mapToEventFullDto(
                        event,
                        requests.getOrDefault(event.getId(), 0),
                        views.getOrDefault(event.getId(), 0L),
                        comments.getOrDefault(event.getId(), new ArrayList<>())
                        )
                )
                .collect(Collectors.toList());
    }
}