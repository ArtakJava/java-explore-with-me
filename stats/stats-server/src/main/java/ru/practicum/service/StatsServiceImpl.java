package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.EndpointMapper;
import ru.practicum.constantManager.ConstantManager;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.exception.NotValidDateException;
import ru.practicum.messageManager.MessageManager;
import ru.practicum.model.EndpointHit;
import ru.practicum.repository.StatsRepository;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Override
    public EndpointHitDto hit(EndpointHitDto endpointHitDto) {
        EndpointHit result = statsRepository.save(EndpointMapper.mapToEndpointHit(endpointHitDto));
        log.info(MessageManager.POST_REQUEST_HIT_SUCCESS, result);
        return EndpointMapper.mapToEndpointHitDto(result);
    }

    @Override
    public List<ViewStatsDto> stats(String startInStr, String endInStr, String[] uris, boolean unique) {
        LocalDateTime start = LocalDateTime.parse(
                URLDecoder.decode(startInStr, StandardCharsets.UTF_8),
                ConstantManager.formatter
        );
        LocalDateTime end = LocalDateTime.parse(
                URLDecoder.decode(endInStr, StandardCharsets.UTF_8),
                ConstantManager.formatter
        );
        dateIsValid(start, end);
        List<ViewStatsDto> result;
        if (uris != null && isNotHaveEventInUris(uris)) {
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
        log.info(MessageManager.GET_REQUEST_STATS_SUCCESS, start, end);
        return result;
    }

    private boolean isNotHaveEventInUris(String[] uris) {
        return !Arrays.asList(uris).contains("/events");
    }

    private void dateIsValid(LocalDateTime start, LocalDateTime end) {
        if (end.isBefore(start)) {
            throw new NotValidDateException(MessageManager.END_BEFORE_START);
        } else if (!start.isBefore(end)) {
            throw new NotValidDateException(MessageManager.START_EQUAL_END);
        }
    }
}