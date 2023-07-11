package ru.practicum.service.pub.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.event.pageParameter.PageRequestCustom;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.messageManager.InfoMessageManager;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.service.admin.category.CategoryMapper;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryPublicServiceImpl implements CategoryPublicService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> getCategories(PageRequestCustom pageRequestCustom) {
        Page<Category> categories = categoryRepository.findAll(pageRequestCustom);
        log.info(InfoMessageManager.SUCCESS_GET_ALL_CATEGORIES);
        return categories.stream()
                .map(CategoryMapper::mapToCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getById(long catId) {
        Category category = categoryRepository.getReferenceById(catId);
        log.info(InfoMessageManager.SUCCESS_GET_CATEGORY, catId);
        return CategoryMapper.mapToCategoryDto(category);
    }
}