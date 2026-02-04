package com.internal.feature.product.service;

import com.internal.feature.product.dto.request.AllBrandRequestDto;
import com.internal.feature.product.dto.request.BrandRequestDto;
import com.internal.feature.product.dto.response.AllBrandResponseDto;
import com.internal.feature.product.dto.response.BrandResponseDto;

public interface BrandService {
    BrandResponseDto createBrand(BrandRequestDto requestDto);

    BrandResponseDto updateBrand(Long id, BrandRequestDto requestDto);

    BrandResponseDto getBrandById(Long id);

    AllBrandResponseDto getAllBrands(AllBrandRequestDto requestDto);

    BrandResponseDto deleteBrand(Long id);
}
