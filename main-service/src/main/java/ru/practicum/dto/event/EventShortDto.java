package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ru.practicum.constantManager.ConstantManager;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.user.UserShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
public class EventShortDto {
    @NotBlank
    private String annotation;
    @NotBlank
    private CategoryDto category;
    private long confirmedRequests;
    @NotBlank
    @JsonFormat(pattern = ConstantManager.datePattern)
    private LocalDateTime eventDate;
    private long id;
    @NotBlank
    private UserShortDto initiator;
    @NotNull
    private Boolean paid;
    @NotBlank
    private String title;
    private long views;
}