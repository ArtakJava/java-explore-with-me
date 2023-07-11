package ru.practicum.constantManager;

import java.time.format.DateTimeFormatter;

public class ConstantManager {
    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    public static final String DEFAULT_UNIQUE_PARAMETER = "false";
}