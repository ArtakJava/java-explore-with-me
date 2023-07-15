package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.dto.Location;
import ru.practicum.constantManager.ConstantManager;
import ru.practicum.messageManager.ErrorMessageManager;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventRequest {
    @Size(min = 20, max = 2000, message = ErrorMessageManager.EVENT_ANNOTATION_MIN_20_MAX_2000)
    protected String annotation;
    protected Long category;
    @Size(min = 20, max = 7000, message = ErrorMessageManager.EVENT_DESCRIPTION_MIN_20_MAX_7000)
    protected String description;
    @JsonFormat(pattern = ConstantManager.DATE_PATTERN)
    protected LocalDateTime eventDate;
    protected Location location;
    protected Boolean paid;
    protected Integer participantLimit;
    protected Boolean requestModeration;
    @Size(min = 3, max = 120, message = ErrorMessageManager.EVENT_TITLE_MIN_3_MAX_120)
    protected String title;
}