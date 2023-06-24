package ru.practicum;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.model.EndpointHit;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EndpointMapper implements Serializable {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static EndpointHitDto mapToEndpointHitDto(EndpointHit endpointHit) {
        return EndpointHitDto.builder()
                .app(endpointHit.getApp())
                .uri(endpointHit.getUrl() + endpointHit.getEventId())
                .ip(endpointHit.getIp())
                .timestamp(endpointHit.getTimestamp().toString())
                .build();
    }

    public static EndpointHit mapToEndpointHit(EndpointHitDto endpointHitDto) {
        return EndpointHit.builder()
                .app(endpointHitDto.getApp())
                .url(getUrl(endpointHitDto.getUri()))
                .eventId(getEventId(endpointHitDto.getUri()))
                .ip(endpointHitDto.getIp())
                .timestamp(LocalDateTime.parse(endpointHitDto.getTimestamp(), formatter))
                .build();
    }

    private static String getUrl(String uri) {
        return uri.substring(0, uri.lastIndexOf("/"));
    }

    private static String getEventId(String uri) {
        return uri.substring(uri.lastIndexOf("/") + 1);
    }
}