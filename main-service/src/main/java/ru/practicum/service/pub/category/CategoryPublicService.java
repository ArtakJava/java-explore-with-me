package ru.practicum.service.pub.category;

import ru.practicum.PageRequestCustom;
import ru.practicum.dto.category.CategoryDto;

import java.util.List;

public interface CategoryPublicService {

    List<CategoryDto> getCategories(PageRequestCustom pageRequestCustom);

    CategoryDto getById(long catId);
}