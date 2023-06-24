package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.messageManager.MessageHolder;
import ru.practicum.service.StatsService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StatsController {
    private final StatsService service;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDto hit(@Valid @RequestBody EndpointHitDto endpointHitDto) {
        log.info(MessageHolder.POST_REQUEST_HIT, endpointHitDto);
        return service.hit(endpointHitDto);
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> stats(@RequestParam String start,
                                    @RequestParam String end,
                                    @RequestParam(required = false) String[] uris,
                                    @RequestParam(required = false, defaultValue = "false") boolean unique) {
        log.info(MessageHolder.GET_REQUEST_STATS, start, end);
        return service.stats(start, end, uris, unique);
    }
}