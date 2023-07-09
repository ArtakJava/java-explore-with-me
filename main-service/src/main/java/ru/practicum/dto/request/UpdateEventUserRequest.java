package ru.practicum.dto.request;

import lombok.*;
import ru.practicum.dto.enums.UserStateAction;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateEventUserRequest extends UpdateEventRequest {
    private UserStateAction stateAction;
}