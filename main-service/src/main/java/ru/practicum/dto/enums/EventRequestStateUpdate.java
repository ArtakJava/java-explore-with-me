package ru.practicum.dto.enums;

public enum EventRequestStateUpdate {
    CONFIRMED("Запрос подтвержден"),
    PENDING("Запрос подтвержден"),
    REJECTED("Запрос отклонен");

    EventRequestStateUpdate(String description) {
    }
}