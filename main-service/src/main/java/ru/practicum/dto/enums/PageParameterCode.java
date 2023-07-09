package ru.practicum.dto.enums;

public enum PageParameterCode {
    ONLY_CATEGORY("Поиск событий по категориям"),
    WITHOUT_DATES("Поиск событий без диапазона дат"),
    WITHOUT_PARAMETERS("Поиск событий без фильтрации"),
    WITH_ALL_PARAMETERS("Поиск событий со всеми параметрами фильтрации"),
    USERS_AND_CATEGORIES("Поиск событий по пользователям и категориям");

    PageParameterCode(String description) {
    }
}