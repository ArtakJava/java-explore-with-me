package ru.practicum.dto.event.pageParameter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.dto.enums.PageParameterCode;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventAdminPageParameter {
    private long[] users;
    private String[] states;
    private long[] categories;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private PageRequestCustom pageRequest;

    public PageParameterCode getPageParameterCode() {
        if (users != null && states != null && categories != null && rangeStart != null && rangeEnd != null) {
            return PageParameterCode.WITH_ALL_PARAMETERS;
        } else if (users != null && states != null && categories != null && rangeStart == null && rangeEnd == null) {
            return PageParameterCode.WITHOUT_DATES;
        } else if (users != null && states == null && categories != null && rangeStart == null && rangeEnd == null) {
            return PageParameterCode.USERS_AND_CATEGORIES;
        } else if (users == null && states == null && categories != null && rangeStart == null && rangeEnd == null) {
            return PageParameterCode.ONLY_CATEGORY;
        }
        return PageParameterCode.WITHOUT_PARAMETERS;
    }
}