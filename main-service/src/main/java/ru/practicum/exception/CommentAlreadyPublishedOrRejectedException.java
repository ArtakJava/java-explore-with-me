package ru.practicum.exception;

public class CommentAlreadyPublishedOrRejectedException extends RuntimeException  {
    public CommentAlreadyPublishedOrRejectedException(String message) {
        super(message);
    }
}