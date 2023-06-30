package ru.practicum;

public enum RequestStatus {
    CONFIRMED("Запрос подтвержден"),
    REJECTED("Запрос отклонен");

    RequestStatus(String description) {
    }
}