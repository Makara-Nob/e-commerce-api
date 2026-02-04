package com.internal.feature.inventory.controller;

import com.internal.exceptions.response.ApiResponse;
import com.internal.feature.inventory.dto.request.AllCategoryRequestDto;
import com.internal.feature.inventory.dto.request.CategoryRequestDTO;
import com.internal.feature.inventory.dto.request.CategoryUpdateDto;
import com.internal.feature.inventory.dto.response.AllCategoryResponseDto;
import com.internal.feature.inventory.dto.response.CategoryDTO;
import com.internal.feature.inventory.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // ----------------- CRUD -----------------
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryDTO>> createCategory(
            @Valid @RequestBody CategoryRequestDTO requestDTO) {
        CategoryDTO responseDTO = categoryService.createCategory(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Category created successfully", responseDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDTO>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryUpdateDto requestDTO) {
        CategoryDTO responseDTO = categoryService.updateCategory(id, requestDTO);
        return ResponseEntity.ok(ApiResponse.success("Category updated successfully", responseDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDTO>> getCategoryById(@PathVariable Long id) {
        CategoryDTO responseDTO = categoryService.getCategoryById(id);
        return ResponseEntity.ok(ApiResponse.success("Category retrieved successfully", responseDTO));
    }

    @PostMapping("/all")
    public ResponseEntity<ApiResponse<AllCategoryResponseDto>> getAllCategory(@RequestBody AllCategoryRequestDto requestDto) {
        return ResponseEntity.ok(ApiResponse.success("Category fetch successfully",  categoryService.getAllCategory(requestDto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDTO>> deleteCategory(@PathVariable Long id) {
        CategoryDTO categoryDTO = categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success("Category deleted successfully", categoryDTO));
    }
}
