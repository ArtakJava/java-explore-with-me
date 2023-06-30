package ru.practicum.messageManager;

public class ErrorMessageManager {
    public static final String CATEGORY_NAME_EMPTY = "Имя категории не может быть пустым.";
    public static final String CATEGORY_NAME_MIN_1_MAX_50 = "Имя категории должно быть в диапазоне 1 - 50 символов.";
    public static final String USER_NAME_EMPTY = "Имя пользователя не может быть пустым.";
    public static final String USER_NAME_MIN_2_MAX_250 = "Имя пользователя должно быть в диапазоне 2 - 250 символов.";
    public static final String USER_EMAIL = "Некорректный email.";
    public static final String USER_EMAIL_MIN_6_MAX_254 = "Email должен быть в диапазоне 6 - 254 символа.";
    public static final String EVENT_ANNOTATION_MIN_20_MAX_2000 =
            "Аннотация события должна быть в диапазоне 20 - 2000 символов.";
    public static final String EVENT_DESCRIPTION_MIN_20_MAX_7000 =
            "Описание события должно быть в диапазоне 20 - 7000 символов.";
    public static final String EVENT_TITLE_MIN_3_MAX_120 =
            "Заголовок события должен быть в диапазоне 3 - 120 символов.";
}