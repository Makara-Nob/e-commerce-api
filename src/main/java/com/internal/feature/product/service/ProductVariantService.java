package com.internal.feature.product.service;

import com.internal.feature.product.dto.request.ProductVariantRequestDto;
import com.internal.feature.product.dto.response.ProductVariantResponseDto;

import java.util.List;

public interface ProductVariantService {

    ProductVariantResponseDto createVariant(Long productId, ProductVariantRequestDto dto);

    ProductVariantResponseDto updateVariant(Long variantId, ProductVariantRequestDto dto);

    void deleteVariant(Long variantId);

    List<ProductVariantResponseDto> getVariantsByProductId(Long productId);

    ProductVariantResponseDto getVariantById(Long variantId);

    List<ProductVariantResponseDto> getActiveVariantsByProductId(Long productId);
}
