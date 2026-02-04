package com.internal.feature.inventory.service;

import com.internal.feature.inventory.dto.request.CategoryRequestDTO;
import com.internal.feature.inventory.dto.request.AllCategoryRequestDto;
import com.internal.feature.inventory.dto.request.CategoryUpdateDto;
import com.internal.feature.inventory.dto.response.AllCategoryResponseDto;
import com.internal.feature.inventory.dto.response.CategoryDTO;

public interface CategoryService {

    CategoryDTO createCategory(CategoryRequestDTO requestDTO);

    CategoryDTO updateCategory(Long id, CategoryUpdateDto requestDTO);

    CategoryDTO getCategoryById(Long id);
    
    AllCategoryResponseDto getAllCategory(AllCategoryRequestDto requestDto);

    CategoryDTO deleteCategory(Long id);
}