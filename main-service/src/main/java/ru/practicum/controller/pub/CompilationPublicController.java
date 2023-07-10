package ru.practicum.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.pageParameter.PageRequestCustom;
import ru.practicum.constantManager.ConstantManager;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.messageManager.InfoMessageManager;
import ru.practicum.service.pub.compilation.CompilationPublicService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping("/compilations")
@Slf4j
@RequiredArgsConstructor
public class CompilationPublicController {
    private final CompilationPublicService service;

    @GetMapping
    public List<CompilationDto> getAll(
            @RequestParam(defaultValue = ConstantManager.DEFAULT_PAGE_PARAMETER_PINNED_FOR_GET_ALL_COMPILATIONS) boolean pinned,
            @RequestParam(defaultValue = ConstantManager.DEFAULT_PAGE_PARAMETER_FROM) @PositiveOrZero int from,
            @RequestParam(defaultValue = ConstantManager.DEFAULT_SIZE_OF_PAGE_COMPILATIONS) @Positive int size) {
        log.info(InfoMessageManager.GET_ALL_COMPILATIONS_REQUEST);
        return service.getAll(pinned, new PageRequestCustom(from, size, ConstantManager.SORT_COMPILATIONS_BY_ID_DESC));
    }

    @GetMapping("/{compId}")
    public CompilationDto getById(@PathVariable long compId) {
        log.info(InfoMessageManager.GET_COMPILATION_REQUEST, compId);
        return service.getById(compId);
    }
}