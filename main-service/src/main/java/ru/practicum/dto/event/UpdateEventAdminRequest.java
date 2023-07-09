package ru.practicum.dto.event;

import lombok.*;
import ru.practicum.enums.AdminStateAction;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateEventAdminRequest extends UpdateEventRequest {
    private AdminStateAction stateAction;
}