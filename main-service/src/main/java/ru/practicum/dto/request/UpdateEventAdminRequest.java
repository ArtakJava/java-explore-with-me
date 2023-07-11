package ru.practicum.dto.request;

import lombok.*;
import ru.practicum.dto.enums.AdminStateAction;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateEventAdminRequest extends UpdateEventRequest {
    private AdminStateAction stateAction;
}