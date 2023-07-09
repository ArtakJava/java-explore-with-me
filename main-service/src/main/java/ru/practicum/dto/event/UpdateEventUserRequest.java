package ru.practicum.dto.event;

import lombok.*;
import ru.practicum.UserStateAction;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateEventUserRequest extends UpdateEventRequest {
    private UserStateAction stateAction;
}