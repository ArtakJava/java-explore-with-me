package ru.practicum.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import ru.practicum.constantManager.ConstantManager;
import ru.practicum.dto.enums.ModerationState;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class CommentFullDto extends CommentShortDto {
    @JsonFormat(pattern = ConstantManager.DATE_PATTERN)
    private final LocalDateTime createdOn;
    private ModerationState state;
}