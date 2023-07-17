package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.constantManager.ConstantManager;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.dto.comment.UpdateCommentRequest;
import ru.practicum.dto.enums.ModerationState;
import ru.practicum.dto.event.UpdateEventRequest;
import ru.practicum.dto.enums.RequestState;
import ru.practicum.exception.EventDateException;
import ru.practicum.exception.EventParticipantLimitException;
import ru.practicum.messageManager.ErrorMessageManager;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.model.ParticipationRequest;
import ru.practicum.repository.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
public class AbstractServiceImpl implements AbstractService {
    protected final RequestRepository requestRepository;
    protected final EventRepository eventRepository;
    protected final UserRepository userRepository;
    protected final CategoryRepository categoryRepository;
    protected final CompilationRepository compilationRepository;
    protected final CommentRepository commentRepository;

    @Override
    public void validateEventDate(LocalDateTime eventDate) {
        if (eventDate != null && eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new EventDateException(ErrorMessageManager.EVENT_DATE_IS_BEFORE_TWO_HOURS);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public long getConfirmedRequestsCount(long eventId) {
        return requestRepository.findConfirmedParticipationCount(eventId);
    }

    @Transactional(readOnly = true)
    @Override
    public Map<Long, Integer> getConfirmedRequestsCountByEvent(List<Event> events) {
        List<ParticipationRequest> requests = requestRepository.findByEventIdInAndState(
                events.stream()
                        .map(Event::getId)
                        .collect(Collectors.toSet()),
                RequestState.CONFIRMED
        );
        Map<Long, List<ParticipationRequest>> confirmedRequestsByEvent = requests.stream()
                .collect(Collectors.groupingBy(request -> request.getEvent().getId()));
        return confirmedRequestsByEvent.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entrySet -> entrySet.getValue().size()));
    }

    @Transactional(readOnly = true)
    @Override
    public Map<Long, Integer> getPublishedCommentsCountByEvent(List<Event> events) {
        List<Comment> comments = commentRepository.findByEventIdInAndState(
                events.stream()
                        .map(Event::getId)
                        .collect(Collectors.toSet()),
                ModerationState.PUBLISHED
        );
        Map<Long, List<Comment>> commentsByEvent = comments.stream()
                .collect(Collectors.groupingBy(comment -> comment.getEvent().getId()));
        return commentsByEvent.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entrySet -> entrySet.getValue().size()));
    }

    @Override
    public Map<Long, Long> getViewsByEventId(List<Event> events, boolean unique) {
        List<ViewStatsDto> viewsStatsDto = new ArrayList<>();
        if (getStartViewsStats(events) != null) {
            ResponseEntity<Object[]> responseEntity = ConstantManager.STATS_CLIENT.stats(
                    getStartViewsStats(events).format(ConstantManager.DATE_TIME_FORMATTER),
                    LocalDateTime.now().plusMinutes(1).format(ConstantManager.DATE_TIME_FORMATTER),
                    getUris(events),
                    unique
            );
            if (responseEntity.getBody() != null) {
                viewsStatsDto = Arrays.stream(responseEntity.getBody())
                        .map(object -> ConstantManager.MAPPER.convertValue(object, ViewStatsDto.class))
                        .collect(Collectors.toList());
            }
        }
        return viewsStatsDto.stream()
                .collect(Collectors.toMap(
                        viewStatsDto -> getEventIdFromUri(viewStatsDto.getUri()),
                        ViewStatsDto::getHits
                        )
                );
    }

    @Override
    public long getViews(Event event, boolean unique) {
        long views = 0;
        if (event.getPublishedOn() != null) {
            ResponseEntity<Object[]> responseEntity = ConstantManager.STATS_CLIENT.stats(
                    event.getPublishedOn().format(ConstantManager.DATE_TIME_FORMATTER),
                    LocalDateTime.now().plusMinutes(1).format(ConstantManager.DATE_TIME_FORMATTER),
                    new String[]{ConstantManager.URI_PREFIX + event.getId()},
                    unique
            );
            if (responseEntity.hasBody()) {
                views = Arrays.stream(Objects.requireNonNull(responseEntity.getBody()))
                        .map(object -> ConstantManager.MAPPER.convertValue(object, ViewStatsDto.class))
                        .map(ViewStatsDto::getHits)
                        .findFirst()
                        .orElse(0L);
            }
        }
        return views;
    }

    @Transactional(readOnly = true)
    @Override
    public void validateParticipantLimit(Event event) {
        if (requestRepository.findConfirmedParticipationCount(event.getId()).equals(event.getParticipantLimit())) {
            throw new EventParticipantLimitException(
                    String.format(ErrorMessageManager.EVENT_PARTICIPANT_LIMIT_REACHED, event.getId())
            );
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Event getUpdatedEvent(Event event, UpdateEventRequest updateEventRequest) {
        if (updateEventRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventRequest.getAnnotation());
        }
        if (updateEventRequest.getCategory() != null) {
            event.setCategory(categoryRepository.getReferenceById(updateEventRequest.getCategory()));
        }
        if (updateEventRequest.getDescription() != null) {
            event.setDescription(updateEventRequest.getDescription());
        }
        if (updateEventRequest.getEventDate() != null) {
            event.setEventDate(updateEventRequest.getEventDate());
        }
        if (updateEventRequest.getLocation() != null) {
            event.setLat(updateEventRequest.getLocation().getLat());
            event.setLon(updateEventRequest.getLocation().getLon());
        }
        if (updateEventRequest.getPaid() != null) {
            event.setPaid(updateEventRequest.getPaid());
        }
        if (updateEventRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventRequest.getParticipantLimit());
        }
        if (updateEventRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventRequest.getRequestModeration());
        }
        if (updateEventRequest.getTitle() != null) {
            event.setTitle(updateEventRequest.getTitle());
        }
        return event;
    }

    @Override
    public Comment getUpdatedComment(Comment comment, UpdateCommentRequest updateCommentRequest) {
        if (updateCommentRequest.getText() != null) {
            comment.setText(updateCommentRequest.getText());
        }
        return comment;
    }

    @Override
    public List<Comment> getCommentsByEvent(long eventId) {
        return commentRepository.findAllByEventId(eventId);
    }

    @Override
    public Map<Long, List<Comment>> getCommentsByEvents(List<Event> events) {
        List<Comment> comments = commentRepository.findAllByEventIdIn(
                events.stream()
                        .map(Event::getId)
                        .collect(Collectors.toSet())
        );
        return  comments.stream()
                .collect(Collectors.groupingBy(comment -> comment.getEvent().getId()));
    }

    protected String[] getUris(List<Event> events) {
        return events.stream()
                .map(event -> ConstantManager.URI_PREFIX + event.getId())
                .toArray(String[]::new);
    }

    private LocalDateTime getStartViewsStats(List<Event> events) {
        return events.stream()
                .map(Event::getPublishedOn)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);
    }

    private long getEventIdFromUri(String uri) {
        String[] bits = uri.split("/");
        return Integer.parseInt(bits[bits.length - 1]);
    }
}
