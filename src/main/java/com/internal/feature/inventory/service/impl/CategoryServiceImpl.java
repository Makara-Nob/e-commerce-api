package com.internal.feature.inventory.service.impl;

import com.internal.exceptions.error.AlreadyExistException;
import com.internal.exceptions.error.NotFoundException;
import com.internal.feature.inventory.dto.request.CategoryRequestDTO;
import com.internal.feature.inventory.dto.request.AllCategoryRequestDto;
import com.internal.feature.inventory.dto.request.CategoryUpdateDto;
import com.internal.feature.inventory.dto.response.AllCategoryResponseDto;
import com.internal.feature.inventory.dto.response.CategoryDTO;
import com.internal.feature.inventory.mapper.CategoryMapper;
import com.internal.feature.inventory.model.Category;
import com.internal.feature.inventory.repository.CategoryRepository;
import com.internal.feature.inventory.service.CategoryService;
import com.internal.feature.inventory.specification.CategorySpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public CategoryDTO createCategory(CategoryRequestDTO requestDTO) {
        log.info("Request to create category: name='{}', code='{}'", requestDTO.getName(), requestDTO.getCode());

        try {
            validateCreateCategory(requestDTO);

            Category category = categoryMapper.toEntity(requestDTO);
            Category savedCategory = categoryRepository.save(category);

            log.info("Category created successfully: id={}, name='{}'", savedCategory.getId(), savedCategory.getName());
            return categoryMapper.toDto(savedCategory);

        } catch (Exception e) {
            log.error("Error creating category: name='{}', code='{}', message={}",
                    requestDTO.getName(), requestDTO.getCode(), e.getMessage(), e);
            throw e;
        }
    }

    private void validateCreateCategory(CategoryRequestDTO requestDTO) {
        if (categoryRepository.existsByName(requestDTO.getName())) {
            log.warn("Category creation failed — duplicate name: {}", requestDTO.getName());
            throw new AlreadyExistException("Category with name '" + requestDTO.getName() + "' already exists");
        }

        if (categoryRepository.existsByCode(requestDTO.getCode())) {
            log.warn("Category creation failed — duplicate code: {}", requestDTO.getCode());
            throw new AlreadyExistException("Category with code '" + requestDTO.getCode() + "' already exists");
        }
    }

    @Override
    public CategoryDTO updateCategory(Long id, CategoryUpdateDto requestDTO) {
        log.info("Request to update category: id={}, name='{}', code='{}'", id, requestDTO.getName(), requestDTO.getCode());

        try {
            Category existingCategory = categoryRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Category not found for update: id={}", id);
                        return new NotFoundException("Category not found with ID: " + id);
                    });

            validateUpdateCategory(id, requestDTO, existingCategory);

            categoryMapper.updateEntity(requestDTO, existingCategory);
            Category updatedCategory = categoryRepository.save(existingCategory);

            log.info("Category updated successfully: id={}, name='{}'", updatedCategory.getId(), updatedCategory.getName());
            return categoryMapper.toDto(updatedCategory);

        } catch (Exception e) {
            log.error("Error updating category id={}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    private void validateUpdateCategory(Long id, CategoryUpdateDto requestDTO, Category existingCategory) {
        if (!existingCategory.getName().equals(requestDTO.getName())
                && categoryRepository.existsByNameAndIdNot(requestDTO.getName(), id)) {
            log.warn("Category update failed — duplicate name: {}", requestDTO.getName());
            throw new AlreadyExistException("Category with name '" + requestDTO.getName() + "' already exists");
        }

        if (!existingCategory.getCode().equals(requestDTO.getCode())
                && categoryRepository.existsByCodeAndIdNot(requestDTO.getCode(), id)) {
            log.warn("Category update failed — duplicate code: {}", requestDTO.getCode());
            throw new AlreadyExistException("Category with code '" + requestDTO.getCode() + "' already exists");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDTO getCategoryById(Long id) {
        log.debug("Fetching category by ID: {}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Category not found with ID: {}", id);
                    return new NotFoundException("Category not found with ID: " + id);
                });

        log.debug("Fetched category: id={}, name='{}'", category.getId(), category.getName());
        return categoryMapper.toDto(category);
    }

    @Override
    public AllCategoryResponseDto getAllCategory(AllCategoryRequestDto requestDto) {
        log.debug("Fetching all categories with filter: {}", requestDto);

        Specification<Category> spec = Specification
                .where(CategorySpecification.search(requestDto.getSearch()))
                .and(CategorySpecification.hasStatus(requestDto.getStatus()));

        Pageable pageable = PageRequest.of(requestDto.getPageNo() - 1, requestDto.getPageSize(),
                Sort.by(Sort.Direction.DESC, "createdAt"));


        Page<Category> categories = categoryRepository.findAll(spec, pageable);

        log.info("Fetched {} categories (page {} of {})",
                categories.getNumberOfElements(), categories.getNumber() + 1, categories.getTotalPages());

        List<CategoryDTO> categoryDTOList = categoryMapper.mapToListDto(categories);
        return categoryMapper.mapToPaginateDto(categoryDTOList, categories);
    }

    @Override
    public CategoryDTO deleteCategory(Long id) {
        log.info("Request to delete category: id={}", id);

        try {
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Category not found for deletion: id={}", id);
                        return new NotFoundException("Category not found with ID: " + id);
                    });

            categoryRepository.delete(category);
            log.info("Category deleted successfully: id={}, name='{}'", id, category.getName());

            return categoryMapper.toDto(category);

        } catch (Exception e) {
            log.error("Error deleting category id={}: {}", id, e.getMessage(), e);
            throw e;
        }
    }
}
