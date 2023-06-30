package ru.practicum.service.admin.category;

import ru.practicum.dto.category.CategoryDto;
import ru.practicum.model.Category;

public interface CategoryAdminService {

    CategoryDto create(CategoryDto categoryDto);

    void delete(long catId);

    CategoryDto update(long catId, CategoryDto categoryDtoPatch);

    Category getUpdatedCategory(Category category, Category categoryPatch);
}