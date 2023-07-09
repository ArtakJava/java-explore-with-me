package ru.practicum.service.admin.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.messageManager.InfoMessageManager;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryAdminServiceImpl implements CategoryAdminService {
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto create(CategoryDto categoryDto) {
        Category category = CategoryMapper.mapToCategoryEntity(categoryDto);
        CategoryDto result = CategoryMapper.mapToCategoryDto(categoryRepository.save(category));
        log.info(InfoMessageManager.SUCCESS_CREATE, result);
        return result;
    }

    @Override
    public void delete(long catId) {
        Category category = categoryRepository.getReferenceById(catId);
        categoryRepository.delete(category);
        log.info(InfoMessageManager.SUCCESS_DELETE, category);
    }

    @Override
    public CategoryDto update(long catId, CategoryDto categoryDtoPatch) {
        Category oldCategory = categoryRepository.getReferenceById(catId);
        Category result = categoryRepository.save(getUpdatedCategory(oldCategory, CategoryMapper.mapToCategoryEntity(categoryDtoPatch)));
        CategoryDto resultDto = CategoryMapper.mapToCategoryDto(result);
        log.info(InfoMessageManager.SUCCESS_PATCH, catId, categoryDtoPatch);
        return resultDto;
    }

    private Category getUpdatedCategory(Category category, Category categoryPatch) {
        if (categoryPatch.getName() != null) {
            category.setName(categoryPatch.getName());
        }
        return category;
    }
}