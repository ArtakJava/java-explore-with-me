package ru.practicum.service.admin.category;

import ru.practicum.dto.category.CategoryDto;

public interface CategoryAdminService {

    CategoryDto create(CategoryDto categoryDto);

    void delete(long catId);

    CategoryDto update(long catId, CategoryDto categoryDtoPatch);
}