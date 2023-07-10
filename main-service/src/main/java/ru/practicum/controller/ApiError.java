package ru.practicum.controller;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ApiError {
    private HttpStatus status;
    private String reason;
    private String message;
    private final LocalDateTime timestamp = LocalDateTime.now();

    public ApiError(HttpStatus status, String reason, String message) {
        this.status = status;
        this.reason = reason;
        this.message = message;
    }
}