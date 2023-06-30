package ru.practicum.constantManager;

import org.springframework.data.domain.Sort;

import java.time.format.DateTimeFormatter;

public class ConstantManager {
    public static final String datePattern = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
    public static final String DEFAULT_SIZE_OF_PAGE_EVENTS = "10";
    public static final Sort SORT_EVENTS_BY_ID_DESC = Sort.by("id").descending();
    public static final String DEFAULT_SIZE_OF_PAGE_USERS = "10";
    public static final Sort SORT_USERS_BY_ID_DESC = Sort.by("id").descending();
}