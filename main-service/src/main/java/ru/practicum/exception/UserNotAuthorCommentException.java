package ru.practicum.exception;

public class UserNotAuthorCommentException extends RuntimeException  {
    public UserNotAuthorCommentException(String message) {
        super(message);
    }
}