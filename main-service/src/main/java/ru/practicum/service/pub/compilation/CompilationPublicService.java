package ru.practicum.service.pub.compilation;

import ru.practicum.service.AbstractService;
import ru.practicum.PageRequestCustom;
import ru.practicum.dto.compilation.CompilationDto;

import java.util.List;

public interface CompilationPublicService extends AbstractService {

    List<CompilationDto> getAll(boolean pinned, PageRequestCustom pageRequestCustom);

    CompilationDto getById(long compId);
}