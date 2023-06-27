package ru.practicum.service;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    EndpointHitDto hit(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> stats(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique);
}