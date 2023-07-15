package ru.practicum.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.messageManager.ErrorMessageManager;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewCommentDto {
    private long id;
    @NotBlank(message = ErrorMessageManager.COMMENT_TEXT_EMPTY)
    @Size(min = 3, max = 1000, message = ErrorMessageManager.COMMENT_TEXT_MIN_3_MAX_1000)
    private String text;
}