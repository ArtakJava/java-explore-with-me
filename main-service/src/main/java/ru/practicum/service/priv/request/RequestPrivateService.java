package ru.practicum.service.priv.request;

import ru.practicum.service.AbstractService;
import ru.practicum.dto.request.ParticipationRequestDto;

import java.util.List;

public interface RequestPrivateService extends AbstractService {

    ParticipationRequestDto createRequest(long userId, long eventId);

    List<ParticipationRequestDto> getAllUserEventRequests(long userId);

    ParticipationRequestDto cancelRequest(long userId, long requestId);
}