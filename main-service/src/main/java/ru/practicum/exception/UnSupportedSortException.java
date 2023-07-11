package ru.practicum.exception;

public class UnSupportedSortException extends RuntimeException  {
    public UnSupportedSortException(String message) {
        super(message);
    }
}