package ru.practicum.dto.comment;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.dto.enums.UserStateAction;

@EqualsAndHashCode(callSuper = true)
@Data
public class UpdateCommentUserRequest extends UpdateCommentRequest {
    private UserStateAction stateAction;
}