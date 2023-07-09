package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.Location;
import ru.practicum.constantManager.ConstantManager;
import ru.practicum.messageManager.ErrorMessageManager;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {
    @NotBlank
    @Size(min = 20, max = 2000, message = ErrorMessageManager.EVENT_ANNOTATION_MIN_20_MAX_2000)
    private String annotation;
    @NotNull
    private Long category;
    @NotBlank
    @Size(min = 20, max = 7000, message = ErrorMessageManager.EVENT_DESCRIPTION_MIN_20_MAX_7000)
    private String description;
    @NotNull
    @JsonFormat(pattern = ConstantManager.DATE_PATTERN)
    private LocalDateTime eventDate;
    private long id;
    @NotNull
    private Location location;
    private Boolean paid = false;
    private Integer participantLimit = 0;
    private Boolean requestModeration = true;
    @NotBlank
    @Size(min = 3, max = 120, message = ErrorMessageManager.EVENT_TITLE_MIN_3_MAX_120)
    private String title;
}