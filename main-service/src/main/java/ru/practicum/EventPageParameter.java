package ru.practicum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class EventPageParameter {
    private int[] users;
    private String[] states;
    private int[] categories;
    private String rangeStart;
    private String rangeEnd;
    private PageRequestCustom pageRequest;
}