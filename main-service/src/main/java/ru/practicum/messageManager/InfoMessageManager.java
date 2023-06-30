package ru.practicum.messageManager;

public class InfoMessageManager {
    public static final String POST_REQUEST = "Получен запрос на сохранение - {}.";
    public static final String DELETE_REQUEST = "Получен запрос на удалении с ID - {}.";
    public static final String PATCH_REQUEST = "Получен запрос на обновление с ID - {} на - {}.";
    public static final String SUCCESS_CREATE = "Запрос на сохранение - {} успешно выполнен.";
    public static final String SUCCESS_DELETE = "Запрос на удаление - {} успешно выполнен.";
    public static final String SUCCESS_PATCH = "Запрос на обновление с ID - {} на - {} успешно выполнен.";
    public static final String GET_ALL_USERS_REQUEST = "Получен запрос на получение списка всех пользователей.";
    public static final String PATCH_REQUEST_EVENT = "Получен запрос на обновление события с ID - {} на - {}.";
    public static final String GET_USER_ALL_EVENTS_REQUEST =
            "Получен запрос на получение списка событий для пользователя с ID - {}.";
    public static final String GET_USER_EVENT_REQUEST = "Получен запрос на получение события ID - {}.";
    public static final String GET_USER_EVENT_REQUESTS =
            "Получен запрос на получение запросов на участие события с ID - {}.";
    public static final String GET_REQUEST_APPROVE_REQUESTS =
            "Получен запрос на изменение статусов запросов на участие события с ID - {}.";
}