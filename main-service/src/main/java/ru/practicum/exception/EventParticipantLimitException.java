package ru.practicum.exception;

public class EventParticipantLimitException extends RuntimeException {
    public EventParticipantLimitException(String message) {
        super(message);
    }
}