package ru.practicum.dto.category;

import lombok.Builder;
import lombok.Data;
import ru.practicum.messageManager.ErrorMessageManager;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class CategoryDto {
    private long id;
    @NotBlank(message = ErrorMessageManager.CATEGORY_NAME_EMPTY)
    @Size(min = 1, max = 50, message = ErrorMessageManager.CATEGORY_NAME_MIN_1_MAX_50)
    private String name;
}