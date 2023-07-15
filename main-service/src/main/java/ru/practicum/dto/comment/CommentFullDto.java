package ru.practicum.dto.comment;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import ru.practicum.dto.enums.ModerationState;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class CommentFullDto extends CommentShortDto {
    private final LocalDateTime createdOn;
    private ModerationState state;
}