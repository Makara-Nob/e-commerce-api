package com.internal.feature.product.controller;

import com.internal.exceptions.response.ApiResponse;
import com.internal.feature.product.dto.request.AllBrandRequestDto;
import com.internal.feature.product.dto.response.AllBrandResponseDto;
import com.internal.feature.product.dto.response.BrandResponseDto;
import com.internal.feature.product.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/public/brands")
@RequiredArgsConstructor
public class PublicBrandController {

    private final BrandService brandService;

    @PostMapping("/all")
    public ResponseEntity<ApiResponse<AllBrandResponseDto>> getAllBrands(@RequestBody AllBrandRequestDto requestDto) {
        return ResponseEntity
                .ok(ApiResponse.success("Brands fetched successfully", brandService.getAllBrands(requestDto)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BrandResponseDto>> getBrandById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Brand retrieved successfully", brandService.getBrandById(id)));
    }
}
