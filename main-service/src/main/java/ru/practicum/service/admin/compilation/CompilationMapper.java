package ru.practicum.service.admin.compilation;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.model.Compilation;
import ru.practicum.service.admin.event.EventMapper;

import java.util.ArrayList;
import java.util.Map;

@UtilityClass
public class CompilationMapper {

    public static Compilation mapToCompilationEntity(NewCompilationDto newCompilationDto) {
        return Compilation.builder()
                .title(newCompilationDto.getTitle())
                .pinned(newCompilationDto.isPinned())
                .events(new ArrayList<>())
                .build();
    }

    public static CompilationDto mapToCompilationDto(Compilation compilation,
                                                     Map<Long, Integer> requests,
                                                     Map<Long, Long> views) {
        return CompilationDto.builder()
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .id(compilation.getId())
                .events(EventMapper.mapToEventsShortDto(
                        compilation.getEvents(),
                        requests,
                        views
                        )
                )
                .build();
    }
}