package ru.practicum.exception;

public class UserAlreadySendRequestException extends RuntimeException {
    public UserAlreadySendRequestException(String message) {
        super(message);
    }
}