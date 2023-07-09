package ru.practicum.enums;

public enum RequestState {
    CONFIRMED("Запрос подтвержден"),
    CANCELED("Запрос отклонен"),
    PENDING("Запрос в режиме ожидания");

    RequestState(String description) {
    }
}