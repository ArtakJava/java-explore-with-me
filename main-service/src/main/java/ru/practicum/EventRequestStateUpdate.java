package ru.practicum;

public enum EventRequestStateUpdate {
    CONFIRMED("Запрос подтвержден"),
    PENDING("Запрос подтвержден"),
    REJECTED("Запрос отклонен");

    EventRequestStateUpdate(String description) {
    }
}