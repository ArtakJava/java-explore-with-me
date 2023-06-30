package ru.practicum;

public enum EventState {
    PENDING("Событие в ожидании модерации"),
    PUBLISHED("Событие опубликовано"),
    CANCELED("Событие отменено");

    EventState(String description) {
    }
}