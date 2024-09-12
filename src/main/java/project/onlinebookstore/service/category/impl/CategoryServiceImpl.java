package project.onlinebookstore.service.category.impl;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.onlinebookstore.dto.category.CategoryDto;
import project.onlinebookstore.dto.category.CreateCategoryRequestDto;
import project.onlinebookstore.exception.EntityNotFoundException;
import project.onlinebookstore.mapper.CategoryMapper;
import project.onlinebookstore.model.Category;
import project.onlinebookstore.repository.category.CategoryRepository;
import project.onlinebookstore.service.category.CategoryService;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .stream()
                .map(categoryMapper::toCategoryDto)
                .toList();
    }

    @Override
    public CategoryDto findById(Long id) {
        Category categoryById =
                categoryRepository.findById(id).orElseThrow(() ->
                        new EntityNotFoundException("Can't find entity by id: " + id));
        return categoryMapper.toCategoryDto(categoryById);
    }

    @Override
    public CategoryDto save(CreateCategoryRequestDto categoryRequestDto) {
        Category category =
                categoryRepository.save(categoryMapper.toCategoryModel(categoryRequestDto));
        return categoryMapper.toCategoryDto(category);
    }

    @Transactional
    @Override
    public CategoryDto update(Long id, CreateCategoryRequestDto categoryDto) {
        Category category =
                categoryRepository.findById(id).orElseThrow(() ->
                        new EntityNotFoundException("Can't find entity by id: " + id));
        categoryMapper.updateCategoryFromDto(categoryDto, category);
        return categoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
