package com.internal.feature.product.service;

import com.internal.feature.product.dto.request.PromotionRequestDto;
import com.internal.feature.product.dto.response.PromotionResponseDto;

import java.util.List;

public interface PromotionService {

    PromotionResponseDto createPromotion(PromotionRequestDto dto);

    PromotionResponseDto updatePromotion(Long promotionId, PromotionRequestDto dto);

    void deletePromotion(Long promotionId);

    List<PromotionResponseDto> getAllPromotions();

    PromotionResponseDto getPromotionById(Long promotionId);

    List<PromotionResponseDto> getActivePromotionsByProductId(Long productId);

    List<PromotionResponseDto> getCurrentActivePromotions();
}
