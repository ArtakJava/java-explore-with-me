package ru.practicum.messageManager;

public class ErrorMessageManager {
    public static final String CATEGORY_NAME_EMPTY = "Имя категории не может быть пустым.";
    public static final String COMPILATION_TITLE_EMPTY = "Заголовок подборки не может быть пустым.";
    public static final String CATEGORY_NAME_MIN_1_MAX_50 = "Имя категории должно быть в диапазоне 1 - 50 символов.";
    public static final String COMPILATION_TITLE_MIN_1_MAX_50 = "Заголовок подборки должен быть в диапазоне 1 - 50 символов.";
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
    public static final String EVENT_PARTICIPANT_LIMIT_REACHED =
            "На событие с ID - %s достигнут лимит количества участников.";
    public static final String EVENT_PARTICIPANT_OWNER =
            "На событие с ID - %s не может участвовать инициатор события с ID - %s.";
    public static final String EVENT_NOT_PUBLISHED =
            "Событие с ID - %s еще не опубликовано.";
    public static final String EVENT_ALREADY_PUBLISHED =
            "Событие с ID - %s уже опубликовано.";
    public static final String NOT_FOUND =
            "Компонент с ID - %s не найден.";
    public static final String EVENT_DATE_IS_BEFORE_TWO_HOURS =
            "Дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента.";
    public static final String RANGE_START_IS_NOT_BEFORE_RANGE_END =
            "В фильтре поиска событий дата начала указана после даты окончания.";
    public static final String USER_ALREADY_SEND_REQUEST =
            "Пользователь с ID - %s уже отправил запрос на участие в событии с ID - %s.";
    public static final String REQUEST_ALREADY_CONFIRMED =
            "Запрос с ID - %s уже одобрен и не может быть отклонен.";
    public static final String SORT_UNSUPPORTED =
            "Сортировка по параметру - %s не поддерживается.";
    public static final String REQUIRED_OBJECT_NOT_FOUND =
            "The required object was not found.";
    public static final String INCORRECTLY_MADE_REQUEST =
            "Incorrectly made request.";
    public static final String INTERNAL_SERVER_ERROR =
            "Internal server error.";
    public static final String CONFLICT =
            "For the requested operation the conditions are not met.";
}