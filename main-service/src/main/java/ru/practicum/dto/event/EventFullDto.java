package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import ru.practicum.constantManager.ConstantManager;
import ru.practicum.dto.Location;
import ru.practicum.dto.comment.CommentShortDto;
import ru.practicum.dto.enums.ModerationState;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class EventFullDto extends EventDto {
    private final LocalDateTime createdOn;
    private String description;
    @NotNull
    private Location location;
    private Integer participantLimit;
    @JsonFormat(pattern = ConstantManager.DATE_PATTERN)
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private ModerationState state;
    private List<CommentShortDto> comments;
}