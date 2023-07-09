package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import ru.practicum.constantManager.ConstantManager;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.dto.event.UpdateEventRequest;
import ru.practicum.exception.EventDateException;
import ru.practicum.exception.EventParticipantLimitException;
import ru.practicum.messageManager.ErrorMessageManager;
import ru.practicum.model.Event;
import ru.practicum.model.ParticipationRequest;
import ru.practicum.repository.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AbstractServiceImpl implements AbstractService {
    protected final RequestRepository requestRepository;
    protected final EventRepository eventRepository;
    protected final UserRepository userRepository;
    protected final CategoryRepository categoryRepository;
    protected final CompilationRepository compilationRepository;

    @Override
    public void validateEventDate(LocalDateTime eventDate) {
        if (eventDate != null && eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new EventDateException(ErrorMessageManager.EVENT_DATE_IS_BEFORE_TWO_HOURS);
        }
    }

    @Override
    public long getConfirmedRequestsCount(long eventId) {
        return requestRepository.findConfirmedParticipationCount(eventId);
    }

    @Override
    public Map<Long, Integer> getConfirmedRequestsByEvent(List<Event> events) {
        List<ParticipationRequest> requests = requestRepository.findByEventIdInAndState(
                events.stream()
                        .map(Event::getId)
                        .collect(Collectors.toSet()),
                RequestState.CONFIRMED
        );
        Map<Long, List<ParticipationRequest>> confirmedRequestsByEvent = requests.stream()
                .collect(Collectors.groupingBy(request -> request.getEvent().getId()));
        return requests.stream()
                .collect(Collectors.toMap(
                        request -> request.getEvent().getId(),
                        request -> confirmedRequestsByEvent.get(request.getEvent().getId()).size()
                        )
                );
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

    @Override
    public void validateParticipantLimit(Event event) {
        if (requestRepository.findConfirmedParticipationCount(event.getId()).equals(event.getParticipantLimit())) {
            throw new EventParticipantLimitException(
                    String.format(ErrorMessageManager.EVENT_PARTICIPANT_LIMIT_REACHED, event.getId())
            );
        }
    }

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

    private LocalDateTime getStartViewsStats(List<Event> events) {
        return events.stream()
                .map(Event::getPublishedOn)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);
    }

    private String[] getUris(List<Event> events) {
        return events.stream()
                .map(event -> ConstantManager.URI_PREFIX + event.getId())
                .toArray(String[]::new);
    }

    private long getEventIdFromUri(String uri) {
        String[] bits = uri.split("/");
        return Integer.parseInt(bits[bits.length - 1]);
    }
}
