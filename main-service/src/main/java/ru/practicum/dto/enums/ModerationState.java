package ru.practicum.dto.enums;

public enum ModerationState {
    PENDING("В ожидании модерации"),
    PUBLISHED("Опубликовано"),
    CANCELED("Отменено");

    ModerationState(String description) {
    }
}