package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.constantManager.ConstantManager;
import ru.practicum.EndpointHitDto;
import ru.practicum.ViewStatsDto;
import ru.practicum.messageManager.MessageManager;
import ru.practicum.service.StatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StatsController {
    private final StatsService service;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDto hit(@Valid @RequestBody EndpointHitDto endpointHitDto) {
        log.info(MessageManager.POST_REQUEST_HIT, endpointHitDto);
        return service.hit(endpointHitDto);
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> stats(
            @RequestParam @DateTimeFormat(pattern = ConstantManager.datePattern) LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = ConstantManager.datePattern) LocalDateTime end,
            @RequestParam(required = false) String[] uris,
            @RequestParam(defaultValue = "false") boolean unique) {
        log.info(MessageManager.GET_REQUEST_STATS, start, end);
        return service.stats(start, end, uris, unique);
    }
}