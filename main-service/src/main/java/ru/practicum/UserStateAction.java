package ru.practicum;

public enum UserStateAction {
    SEND_TO_REVIEW("Отправлено на проверку"),
    CANCEL_REVIEW("Отменена отправка на проверку");

    UserStateAction(String description) {
    }
}