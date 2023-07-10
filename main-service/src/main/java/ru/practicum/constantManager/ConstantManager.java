package ru.practicum.constantManager;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Sort;
import ru.practicum.StatsClient;

import java.time.format.DateTimeFormatter;

public class ConstantManager {
    public static final String STATS_SERVER_URL = "http://stats-server:9090";
    public static final StatsClient STATS_CLIENT = new StatsClient(STATS_SERVER_URL);
    public static final String APP_NAME = "main-service";
    public static final String URI_PREFIX = "/events/";
    public static final ObjectMapper MAPPER = new ObjectMapper();
    public static final boolean DEFAULT_UNIQUE_FOR_STATS = true;
    public static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    public static final String DEFAULT_SIZE_OF_PAGE_EVENTS = "10";
    public static final Sort SORT_EVENTS_BY_EVENT_DATE = Sort.by("eventDate").descending();
    public static final String DEFAULT_SIZE_OF_PAGE_USERS = "10";
    public static final Sort SORT_USERS_BY_ID_ASC = Sort.by("id").ascending();
    public static final String DEFAULT_SIZE_OF_PAGE_CATEGORIES = "10";
    public static final Sort SORT_CATEGORIES_BY_ID_DESC = Sort.by("id").descending();
    public static final String DEFAULT_SIZE_OF_PAGE_COMPILATIONS = "10";
    public static final Sort SORT_COMPILATIONS_BY_ID_DESC = Sort.by("id").descending();
    public static final String DEFAULT_PAGE_PARAMETER_PINNED_FOR_GET_ALL_COMPILATIONS = "false";
    public static final String DEFAULT_PAGE_PARAMETER_FROM = "0";
    public static final String DEFAULT_PAGE_PARAMETER_ONLY_AVAILABLE_FOR_GET_ALL_EVENTS = "false";
    public static final String DEFAULT_PAGE_PARAMETER_SORT_FOR_GET_ALL_EVENTS = "EVENT_DATE";
}