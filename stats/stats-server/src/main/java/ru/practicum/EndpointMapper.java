package ru.practicum;

import lombok.experimental.UtilityClass;
import ru.practicum.constantManager.ConstantManager;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.model.EndpointHit;

import java.io.Serializable;
import java.time.LocalDateTime;

@UtilityClass
public class EndpointMapper implements Serializable {

    public static EndpointHitDto mapToEndpointHitDto(EndpointHit endpointHit) {
        return EndpointHitDto.builder()
                .app(endpointHit.getApp())
                .uri(endpointHit.getUri())
                .ip(endpointHit.getIp())
                .timestamp(endpointHit.getTimestamp().toString())
                .build();
    }

    public static EndpointHit mapToEndpointHit(EndpointHitDto endpointHitDto) {
        return EndpointHit.builder()
                .app(endpointHitDto.getApp())
                .uri(endpointHitDto.getUri())
                .ip(endpointHitDto.getIp())
                .timestamp(LocalDateTime.parse(endpointHitDto.getTimestamp(), ConstantManager.FORMATTER))
                .build();
    }
}