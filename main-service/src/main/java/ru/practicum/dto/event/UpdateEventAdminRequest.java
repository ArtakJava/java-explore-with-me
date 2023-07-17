package ru.practicum.dto.event;

import lombok.*;
import ru.practicum.dto.enums.AdminEventStateAction;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateEventAdminRequest extends UpdateEventRequest {
    private AdminEventStateAction stateAction;
}