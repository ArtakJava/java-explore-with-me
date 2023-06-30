package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import ru.practicum.Location;
import ru.practicum.UserStateAction;
import ru.practicum.constantManager.ConstantManager;
import ru.practicum.messageManager.ErrorMessageManager;

import javax.validation.constraints.Size;

@Data
@Builder
public class UpdateEventUserRequest {
    @Size(min = 20, max = 2000, message = ErrorMessageManager.EVENT_ANNOTATION_MIN_20_MAX_2000)
    private String annotation;
    private Long category;
    @Size(min = 20, max = 7000, message = ErrorMessageManager.EVENT_DESCRIPTION_MIN_20_MAX_7000)
    private String description;
    @JsonFormat(pattern = ConstantManager.datePattern)
    private String eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private UserStateAction stateAction;
    @Size(min = 3, max = 120, message = ErrorMessageManager.EVENT_TITLE_MIN_3_MAX_120)
    private String title;
}