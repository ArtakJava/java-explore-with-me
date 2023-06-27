package ru.practicum.exception;

public class NotValidDateException extends RuntimeException {
    public NotValidDateException(String message) {
        super(message);
    }
}