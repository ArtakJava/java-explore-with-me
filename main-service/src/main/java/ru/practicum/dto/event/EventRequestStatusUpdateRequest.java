package ru.practicum.dto.event;

import lombok.Builder;
import lombok.Data;
import ru.practicum.RequestStatus;

import java.util.List;

@Data
@Builder
public class EventRequestStatusUpdateRequest {
    private List<Integer> requestIds;
    private RequestStatus status;
}