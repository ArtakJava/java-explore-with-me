package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.EndpointMapper;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.exception.NotValidDateException;
import ru.practicum.messageManager.MessageHolder;
import ru.practicum.model.EndpointHit;
import ru.practicum.repository.StatsRepository;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public EndpointHitDto hit(EndpointHitDto endpointHitDto) {
        EndpointHit result = statsRepository.save(EndpointMapper.mapToEndpointHit(endpointHitDto));
        log.info(MessageHolder.POST_REQUEST_HIT_SUCCESS, result);
        return EndpointMapper.mapToEndpointHitDto(result);
    }

    @Override
    public List<ViewStatsDto> stats(String startInStr, String endInStr, String[] uris, Boolean unique) {
        LocalDateTime start = LocalDateTime.parse(URLDecoder.decode(startInStr, StandardCharsets.UTF_8), formatter);
        LocalDateTime end = LocalDateTime.parse(URLDecoder.decode(endInStr, StandardCharsets.UTF_8), formatter);
        dateIsValid(start, end);
        List<ViewStatsDto> result;
        if (uris != null) {
            if (unique) {
                result = statsRepository.findDistinctByIpAndTimestampBetweenAndEventIdIn(start, end, uris);
            } else {
                result = statsRepository.findByTimestampBetweenAndEventIdIn(start, end, uris);
            }
        } else {
            if (unique) {
                result = statsRepository.findDistinctByIpAndTimestampBetween(start, end);
            } else {
                result = statsRepository.findByTimestampBetween(start, end);
            }
        }
        log.info(MessageHolder.GET_REQUEST_STATS_SUCCESS, start, end);
        return result;
    }

    private void dateIsValid(LocalDateTime start, LocalDateTime end) {
        if (end.isBefore(start)) {
            throw new NotValidDateException(MessageHolder.END_BEFORE_START);
        } else if (!start.isBefore(end)) {
            throw new NotValidDateException(MessageHolder.START_EQUAL_END);
        }
    }
}