package ru.practicum.dto.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.constantManager.ConstantManager;
import ru.practicum.dto.user.UserShortDto;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CommentShortDto {
    protected long id;
    protected String text;
    protected UserShortDto author;
    @JsonFormat(pattern = ConstantManager.DATE_PATTERN)
    protected LocalDateTime publishedOn;
}