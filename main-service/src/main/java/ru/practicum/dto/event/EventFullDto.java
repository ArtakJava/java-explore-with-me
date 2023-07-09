package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.enums.EventState;
import ru.practicum.Location;
import ru.practicum.constantManager.ConstantManager;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.dto.category.CategoryDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventFullDto {
    @NotBlank
    private String annotation;
    @NotNull
    private CategoryDto category;
    private long confirmedRequests;
    private final String createdOn = LocalDateTime.now().format(ConstantManager.DATE_TIME_FORMATTER);
    private String description;
    @NotNull
    @JsonFormat(pattern = ConstantManager.DATE_PATTERN)
    private LocalDateTime eventDate;
    private long id;
    @NotNull
    private UserShortDto initiator;
    @NotNull
    private Location location;
    @NotNull
    private Boolean paid;
    private Integer participantLimit = 0;
    @JsonFormat(pattern = ConstantManager.DATE_PATTERN)
    private LocalDateTime publishedOn;
    private Boolean requestModeration = true;
    private EventState state = EventState.PENDING;
    @NotBlank
    private String title;
    private long views;
}