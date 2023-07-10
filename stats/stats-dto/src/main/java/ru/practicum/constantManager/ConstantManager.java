package ru.practicum.constantManager;

import java.time.format.DateTimeFormatter;

public class ConstantManager {
    public static final String datePattern = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
    public static final String DEFAULT_UNIQUE_PARAMETER = "false";
}