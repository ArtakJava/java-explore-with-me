package ru.practicum;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ApiError {
    private HttpStatus status;
    private String reason;
    private String message;
    private final String timestamp = LocalDateTime.now().toString();
}