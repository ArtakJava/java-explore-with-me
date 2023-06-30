package ru.practicum.dto.user;

import lombok.Builder;
import lombok.Data;
import ru.practicum.messageManager.ErrorMessageManager;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class UserDto {
    private long id;
    @Email(message = ErrorMessageManager.USER_EMAIL)
    @Size(min = 6, max = 254, message = ErrorMessageManager.USER_EMAIL_MIN_6_MAX_254)
    private String email;
    @NotBlank(message = ErrorMessageManager.USER_NAME_EMPTY)
    @Size(min = 2, max = 250, message = ErrorMessageManager.USER_NAME_MIN_2_MAX_250)
    private String name;
}