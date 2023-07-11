package ru.practicum.dto.compilation;

import lombok.Builder;
import lombok.Data;
import ru.practicum.messageManager.ErrorMessageManager;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
public class UpdateCompilationRequest {
    private List<Long> events;
    private Boolean pinned;
    @Size(min = 1, max = 50, message = ErrorMessageManager.COMPILATION_TITLE_MIN_1_MAX_50)
    private String title;
}