package com.internal.feature.inventory.controller;

import com.internal.exceptions.response.ApiResponse;
import com.internal.feature.inventory.dto.request.AllCategoryRequestDto;
import com.internal.feature.inventory.dto.response.AllCategoryResponseDto;
import com.internal.feature.inventory.dto.response.CategoryDTO;
import com.internal.feature.inventory.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/public/categories")
@RequiredArgsConstructor
public class PublicCategoryController {

    private final CategoryService categoryService;

    /**
     * Public endpoint to get all categories with pagination and filtering
     * No authentication required
     */
    @PostMapping("/all")
    public ResponseEntity<ApiResponse<AllCategoryResponseDto>> getAllCategories(
            @RequestBody AllCategoryRequestDto requestDto) {
        return ResponseEntity.ok(
                ApiResponse.success("Category fetch successfully", categoryService.getAllCategory(requestDto)));
    }

    /**
     * Public endpoint to get a single category by ID
     * No authentication required
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDTO>> getCategoryById(@PathVariable Long id) {
        CategoryDTO responseDTO = categoryService.getCategoryById(id);
        return ResponseEntity.ok(
                ApiResponse.success("Category retrieved successfully", responseDTO));
    }
}
