package ru.practicum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.enums.PageParameterCode;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventPublicPageParameter {
    private String text;
    private long[] categories;
    private Boolean paid;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private boolean onlyAvailable;
    private PageRequestCustom pageRequest;
    private String sort;

    public PageParameterCode getPageParameterCode() {
        if (text != null && categories != null && paid != null && rangeStart != null && rangeEnd != null) {
            return PageParameterCode.WITH_ALL_PARAMETERS;
        } else if (text != null && categories != null && paid != null && rangeStart == null && rangeEnd == null) {
            return PageParameterCode.WITHOUT_DATES;
        } else if (text == null && categories != null && paid == null && rangeStart == null && rangeEnd == null) {
            return PageParameterCode.ONLY_CATEGORY;
        }
        return PageParameterCode.WITHOUT_PARAMETERS;
    }
}