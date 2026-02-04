package com.internal.feature.product.controller;

import com.internal.exceptions.response.ApiResponse;
import com.internal.feature.product.dto.request.AllBrandRequestDto;
import com.internal.feature.product.dto.request.BrandRequestDto;
import com.internal.feature.product.dto.response.AllBrandResponseDto;
import com.internal.feature.product.dto.response.BrandResponseDto;
import com.internal.feature.product.service.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @PostMapping
    public ResponseEntity<ApiResponse<BrandResponseDto>> createBrand(
            @Valid @RequestBody BrandRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Brand created successfully", brandService.createBrand(requestDto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BrandResponseDto>> updateBrand(
            @PathVariable Long id,
            @Valid @RequestBody BrandRequestDto requestDto) {
        return ResponseEntity
                .ok(ApiResponse.success("Brand updated successfully", brandService.updateBrand(id, requestDto)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BrandResponseDto>> getBrandById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Brand retrieved successfully", brandService.getBrandById(id)));
    }

    @PostMapping("/all")
    public ResponseEntity<ApiResponse<AllBrandResponseDto>> getAllBrands(@RequestBody AllBrandRequestDto requestDto) {
        return ResponseEntity
                .ok(ApiResponse.success("Brands fetched successfully", brandService.getAllBrands(requestDto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<BrandResponseDto>> deleteBrand(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Brand deleted successfully", brandService.deleteBrand(id)));
    }
}
