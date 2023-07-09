package ru.practicum.controller.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.PageRequestCustom;
import ru.practicum.constantManager.ConstantManager;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.messageManager.InfoMessageManager;
import ru.practicum.service.pub.category.CategoryPublicService;

import java.util.List;

@RestController
@RequestMapping("/categories")
@Slf4j
@RequiredArgsConstructor
public class CategoryPublicController {
    private final CategoryPublicService service;

    @GetMapping
    public List<CategoryDto> getCategories(
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = ConstantManager.DEFAULT_SIZE_OF_PAGE_CATEGORIES) int size) {
        log.info(InfoMessageManager.GET_ALL_CATEGORIES_REQUEST);
        return service.getCategories(new PageRequestCustom(from, size, ConstantManager.SORT_CATEGORIES_BY_ID_DESC));
    }

    @GetMapping("/{catId}")
    public CategoryDto getById(@PathVariable long catId) {
        log.info(InfoMessageManager.GET_CATEGORY_BY_ID_REQUEST, catId);
        return service.getById(catId);
    }
}