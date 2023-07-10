package ru.practicum.service;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import java.util.List;

public interface StatsService {

    EndpointHitDto hit(EndpointHitDto endpointHitDto);

    List<ViewStatsDto> stats(String startInStr, String endInStr, String[] uris, boolean unique);
}