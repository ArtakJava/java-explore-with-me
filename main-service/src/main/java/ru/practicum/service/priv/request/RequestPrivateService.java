package ru.practicum.service.priv.request;

import ru.practicum.AbstractService;
import ru.practicum.dto.event.ParticipationRequestDto;

import java.util.List;

public interface RequestPrivateService extends AbstractService {

    ParticipationRequestDto createRequest(long userId, long eventId);

    List<ParticipationRequestDto> getAllUserEventRequests(long userId);

    ParticipationRequestDto cancelRequest(long userId, long requestId);
}