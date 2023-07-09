package ru.practicum.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.*;
import ru.practicum.constantManager.ConstantManager;
import ru.practicum.dto.event.EventFullDto;
import ru.practicum.dto.event.EventShortDto;
import ru.practicum.dto.event.pageParameter.EventPublicPageParameter;
import ru.practicum.messageManager.InfoMessageManager;
import ru.practicum.service.pub.event.EventPublicService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/events")
@Slf4j
@RequiredArgsConstructor
public class EventPublicController {
    private final EventPublicService service;

    @GetMapping
    public List<EventShortDto> getEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) long[] categories,
            @RequestParam(required = false) boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = ConstantManager.DATE_PATTERN) LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = ConstantManager.DATE_PATTERN) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "false") boolean onlyAvailable,
            @RequestParam(defaultValue = "EVENT_DATE") String sort,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = ConstantManager.DEFAULT_SIZE_OF_PAGE_EVENTS) int size,
            HttpServletRequest request) {
        log.info(InfoMessageManager.GET_ALL_EVENTS_REQUEST);
        return service.getEvents(
                new EventPublicPageParameter(
                        text,
                        categories,
                        paid,
                        rangeStart,
                        rangeEnd,
                        onlyAvailable,
                        new PageRequestCustom(from, size, ConstantManager.SORT_EVENTS_BY_EVENT_DATE),
                        sort
                ),
                request
        );
    }

    @GetMapping("/{eventId}")
    public EventFullDto getById(@PathVariable long eventId, HttpServletRequest request) {
        log.info(InfoMessageManager.GET_EVENT_BY_ID_REQUEST, eventId);
        return service.getById(eventId, request);
    }
}