package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ru.practicum.EventState;
import ru.practicum.Location;
import ru.practicum.constantManager.ConstantManager;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.dto.category.CategoryDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class EventFullDto {
    @NotBlank
    private String annotation;
    @NotBlank
    private CategoryDto category;
    private long confirmedRequests;
    private final String createdOn = LocalDateTime.now().format(ConstantManager.formatter);
    private String description;
    @NotBlank
    @JsonFormat(pattern = ConstantManager.datePattern)
    private LocalDateTime eventDate;
    private long id;
    @NotBlank
    private UserShortDto initiator;
    @NotBlank
    private Location location;
    @NotNull
    private Boolean paid;
    private Integer participantLimit = 0;
    @JsonFormat(pattern = ConstantManager.datePattern)
    private LocalDateTime publishedOn;
    private Boolean requestModeration = true;
    private EventState state = EventState.PENDING;
    @NotBlank
    private String title;
    private long views;
}