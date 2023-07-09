package ru.practicum.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.pageParameter.PageRequestCustom;
import ru.practicum.constantManager.ConstantManager;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.messageManager.InfoMessageManager;
import ru.practicum.service.pub.compilation.CompilationPublicService;

import java.util.List;

@RestController
@RequestMapping("/compilations")
@Slf4j
@RequiredArgsConstructor
public class CompilationPublicController {
    private final CompilationPublicService service;

    @GetMapping
    public List<CompilationDto> getAll(
            @RequestParam(defaultValue = "false") boolean pinned,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = ConstantManager.DEFAULT_SIZE_OF_PAGE_COMPILATIONS) int size) {
        log.info(InfoMessageManager.GET_ALL_COMPILATIONS_REQUEST);
        return service.getAll(pinned, new PageRequestCustom(from, size, ConstantManager.SORT_COMPILATIONS_BY_ID_DESC));
    }

    @GetMapping("/{compId}")
    public CompilationDto getById(@PathVariable long compId) {
        log.info(InfoMessageManager.GET_COMPILATION_REQUEST, compId);
        return service.getById(compId);
    }
}