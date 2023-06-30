package ru.practicum.controller;

public class ErrorResponse {
    private final String error;

    public ErrorResponse(String message) {
        this.error = message;
    }
}