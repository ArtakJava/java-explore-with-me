package ru.practicum.service.pub.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.constantManager.ConstantManager;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.enums.EventPublicSort;
import ru.practicum.dto.enums.ModerationState;
import ru.practicum.dto.enums.PageParameterCode;
import ru.practicum.dto.event.pageParameter.EventPublicPageParameter;
import ru.practicum.dto.event.pageParameter.PageRequestCustom;
import ru.practicum.exception.EventNotPublishedException;
import ru.practicum.exception.RangeParametersException;
import ru.practicum.exception.UnSupportedSortException;
import ru.practicum.messageManager.ErrorMessageManager;
import ru.practicum.messageManager.InfoMessageManager;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.repository.*;
import ru.practicum.service.AbstractServiceImpl;
import ru.practicum.service.admin.event.EventMapper;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@Slf4j
public class EventPublicServiceImpl extends AbstractServiceImpl implements EventPublicService {

    public EventPublicServiceImpl(
            RequestRepository requestRepository,
            EventRepository eventRepository,
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            CompilationRepository compilationRepository,
            CommentRepository commentRepository) {
        super(requestRepository, eventRepository, userRepository, categoryRepository, compilationRepository, commentRepository);
    }

    @Override
    public List<EventShortDto> getEvents(EventPublicPageParameter eventPublicPageParameter, HttpServletRequest request) {
        List<Event> events = new ArrayList<>();
        List<EventShortDto> result;
        validateDates(eventPublicPageParameter);
        PageParameterCode code = eventPublicPageParameter.getPageParameterCode();
        switch (code) {
            case ONLY_CATEGORY:
                events = eventRepository.findByCategoryIdInAndEventDateAfterAndState(
                        eventPublicPageParameter.getCategories(),
                        LocalDateTime.now(),
                        ModerationState.PUBLISHED,
                        eventPublicPageParameter.getPageRequest()
                );
                break;
            case WITHOUT_DATES:
                events = eventRepository
                        .findByAnnotationContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndCategoryIdInAndPaidAndEventDateAfterAndState(
                                eventPublicPageParameter.getText(),
                                eventPublicPageParameter.getText(),
                                eventPublicPageParameter.getCategories(),
                                eventPublicPageParameter.getPaid(),
                                LocalDateTime.now(),
                                ModerationState.PUBLISHED,
                                eventPublicPageParameter.getPageRequest()
                        );
                break;
            case WITHOUT_PARAMETERS:
                events = eventRepository.findByEventDateAfterAndState(
                        LocalDateTime.now(),
                        ModerationState.PUBLISHED,
                        eventPublicPageParameter.getPageRequest()
                );
                break;
            case WITH_ALL_PARAMETERS:
                events = eventRepository
                        .findByAnnotationContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndCategoryIdInAndPaidAndEventDateAfterAndEventDateBeforeAndState(
                                eventPublicPageParameter.getText(),
                                eventPublicPageParameter.getText(),
                                eventPublicPageParameter.getCategories(),
                                eventPublicPageParameter.getPaid(),
                                eventPublicPageParameter.getRangeStart(),
                                eventPublicPageParameter.getRangeEnd(),
                                ModerationState.PUBLISHED,
                                eventPublicPageParameter.getPageRequest()
                        );
                break;
        }
        Arrays.stream(getUris(events)).forEach(uri -> addHit(uri, request));
        String sort = eventPublicPageParameter.getSort();
        if (EventPublicSort.EVENT_DATE.equals(EventPublicSort.valueOf(sort))) {
            result = EventMapper.mapToEventsShortDto(
                    events,
                    getConfirmedRequestsCountByEvent(events),
                    getViewsByEventId(events, ConstantManager.DEFAULT_UNIQUE_FOR_STATS),
                    getPublishedCommentsCountByEvent(events)
            );
        } else if ((EventPublicSort.VIEWS.equals(EventPublicSort.valueOf(sort)))) {
            result = EventMapper.mapToEventsShortDto(events,
                            getConfirmedRequestsCountByEvent(events),
                            getViewsByEventId(events, true),
                            getPublishedCommentsCountByEvent(events)).stream()
                    .sorted(Comparator.comparingLong(EventShortDto::getViews))
                    .collect(Collectors.toList());
        } else {
            throw new UnSupportedSortException(String.format(ErrorMessageManager.SORT_UNSUPPORTED, sort));
        }
        log.info(InfoMessageManager.SUCCESS_GET_ALL_EVENTS);
        return result;
    }

    @Override
    public EventFullDto getById(long eventId, HttpServletRequest request) {
        Event event = eventRepository.getReferenceById(eventId);
        if (event.getAnnotation() == null) {
            throw new EntityNotFoundException(String.format(ErrorMessageManager.NOT_FOUND, eventId));
        }
        if (!event.getState().equals(ModerationState.PUBLISHED)) {
            throw new EventNotPublishedException(String.format(ErrorMessageManager.EVENT_NOT_PUBLISHED, eventId));
        }
        addHit(request.getRequestURI(), request);
        EventFullDto result = EventMapper.mapToEventFullDto(
                event,
                getConfirmedRequestsCount(eventId),
                getViews(event, ConstantManager.DEFAULT_UNIQUE_FOR_STATS),
                getPublishedCommentsByEvent(eventId)
        );
        log.info(InfoMessageManager.SUCCESS_EVENT_REQUEST, event);
        return result;
    }

    private void validateDates(EventPublicPageParameter eventPublicPageParameter) {
        LocalDateTime rangeStart = eventPublicPageParameter.getRangeStart();
        LocalDateTime rangeEnd = eventPublicPageParameter.getRangeEnd();
        if (rangeStart != null && rangeEnd != null && !rangeStart.isBefore(rangeEnd)) {
            throw new RangeParametersException(ErrorMessageManager.RANGE_START_IS_NOT_BEFORE_RANGE_END);
        }
    }

    private void addHit(String uri, HttpServletRequest request) {
        ConstantManager.STATS_CLIENT.hit(
                EndpointHitDto.builder()
                        .app(ConstantManager.APP_NAME)
                        .uri(uri)
                        .ip(request.getRemoteAddr())
                        .timestamp(LocalDateTime.now().format(ConstantManager.DATE_TIME_FORMATTER))
                        .build()
        );
    }

    private List<Comment> getPublishedCommentsByEvent(long eventId) {
        PageRequestCustom pageRequestCustom = new PageRequestCustom(
                Integer.parseInt(ConstantManager.DEFAULT_PAGE_PARAMETER_FROM),
                Integer.parseInt(ConstantManager.DEFAULT_SIZE_OF_PAGE_COMMENTS),
                ConstantManager.SORT_COMMENTS_BY_CREATE_DATE

        );
        return commentRepository.findByEventIdAndState(eventId, ModerationState.PUBLISHED, pageRequestCustom);
    }
}