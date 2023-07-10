package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import ru.practicum.constantManager.ConstantManager;
import ru.practicum.dto.Location;
import ru.practicum.dto.enums.EventState;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class EventFullDto extends EventShortDto {
    private final LocalDateTime createdOn = LocalDateTime.now();
    private String description;
    @NotNull
    private Location location;
    private Integer participantLimit = 0;
    @JsonFormat(pattern = ConstantManager.DATE_PATTERN)
    private LocalDateTime publishedOn;
    private Boolean requestModeration = true;
    private EventState state = EventState.PENDING;
}