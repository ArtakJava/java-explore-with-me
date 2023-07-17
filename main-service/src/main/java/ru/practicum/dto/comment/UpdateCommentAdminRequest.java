package ru.practicum.dto.comment;

import lombok.*;
import ru.practicum.dto.enums.AdminCommentStateAction;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateCommentAdminRequest extends UpdateCommentRequest {
    private AdminCommentStateAction stateAction;
}