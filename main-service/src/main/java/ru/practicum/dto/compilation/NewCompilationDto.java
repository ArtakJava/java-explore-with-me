package ru.practicum.dto.compilation;

import lombok.Data;
import ru.practicum.messageManager.ErrorMessageManager;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
public class NewCompilationDto {
    private List<Long> events = new ArrayList<>();
    private boolean pinned = false;
    @NotBlank(message = ErrorMessageManager.COMPILATION_TITLE_EMPTY)
    @Size(min = 1, max = 50, message = ErrorMessageManager.COMPILATION_TITLE_MIN_1_MAX_50)
    private String title;
}