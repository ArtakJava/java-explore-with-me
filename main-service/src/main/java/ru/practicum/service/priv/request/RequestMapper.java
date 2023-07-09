package ru.practicum.service.priv.request;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.enums.EventRequestStateUpdate;
import ru.practicum.dto.enums.RequestState;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.model.Event;
import ru.practicum.model.ParticipationRequest;
import ru.practicum.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class RequestMapper {
    public static ParticipationRequest mapToRequestEntity(User user, Event event) {
        return ParticipationRequest.builder()
                .requester(user)
                .event(event)
                .state(RequestState.PENDING)
                .createdOn(LocalDateTime.now())
                .build();
    }

    public static ParticipationRequestDto maptoParticipationRequestDto(ParticipationRequest participationRequest) {
        return ParticipationRequestDto.builder()
                .id(participationRequest.getId())
                .event(participationRequest.getEvent().getId())
                .created(participationRequest.getCreatedOn())
                .requester(participationRequest.getRequester().getId())
                .status(participationRequest.getState().toString())
                .build();
    }

    public static ParticipationRequestDto mapAprovetoParticipationRequestDto(ParticipationRequest participationRequest) {
        return ParticipationRequestDto.builder()
                .id(participationRequest.getId())
                .event(participationRequest.getEvent().getId())
                .created(participationRequest.getCreatedOn())
                .requester(participationRequest.getRequester().getId())
                .status(getEventRequestStateUpdate(participationRequest.getState()).toString())
                .build();
    }

    private EventRequestStateUpdate getEventRequestStateUpdate(RequestState status) {
        EventRequestStateUpdate state = EventRequestStateUpdate.CONFIRMED;
        if (status.equals(RequestState.CANCELED)) {
            state = EventRequestStateUpdate.REJECTED;
        } else if (status.equals(RequestState.PENDING)) {
            state = EventRequestStateUpdate.PENDING;
        }
        return state;
    }

    public static List<ParticipationRequestDto> mapToParticipationRequestsDto(List<ParticipationRequest> participationRequests) {
        return participationRequests.stream()
                .map(RequestMapper::maptoParticipationRequestDto)
                .collect(Collectors.toList());
    }
}
