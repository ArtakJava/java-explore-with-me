package ru.practicum;

import lombok.Data;

@Data
public class ErrorResponse {
    private String error;

    public ErrorResponse(String message) {
        this.error = message;
    }
}