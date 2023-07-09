package ru.practicum.exception;

public class EventNotPublishedForRequestException extends RuntimeException {
    public EventNotPublishedForRequestException(String message) {
        super(message);
    }
}