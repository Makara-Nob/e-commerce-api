package com.internal.feature.product.service.impl;

import com.internal.enumation.StatusData;
import com.internal.exceptions.error.NotFoundException;
import com.internal.feature.product.dto.request.PromotionRequestDto;
import com.internal.feature.product.dto.response.PromotionResponseDto;
import com.internal.feature.product.mapper.PromotionMapper;
import com.internal.feature.product.model.Product;
import com.internal.feature.product.model.Promotion;
import com.internal.feature.product.repository.ProductRepository;
import com.internal.feature.product.repository.PromotionRepository;
import com.internal.feature.product.service.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;
    private final ProductRepository productRepository;
    private final PromotionMapper promotionMapper;

    @Override
    @Transactional
    public PromotionResponseDto createPromotion(PromotionRequestDto dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + dto.getProductId()));

        Promotion promotion = promotionMapper.toEntity(dto);
        promotion.setProduct(product);

        Promotion savedPromotion = promotionRepository.save(promotion);
        return promotionMapper.toDto(savedPromotion);
    }

    @Override
    @Transactional
    public PromotionResponseDto updatePromotion(Long promotionId, PromotionRequestDto dto) {
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new NotFoundException("Promotion not found with id: " + promotionId));

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + dto.getProductId()));

        promotionMapper.updateEntityFromDto(dto, promotion);
        promotion.setProduct(product);

        Promotion updatedPromotion = promotionRepository.save(promotion);
        return promotionMapper.toDto(updatedPromotion);
    }

    @Override
    @Transactional
    public void deletePromotion(Long promotionId) {
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new NotFoundException("Promotion not found with id: " + promotionId));

        promotion.setStatus(StatusData.DELETE);
        promotionRepository.save(promotion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromotionResponseDto> getAllPromotions() {
        return promotionRepository.findAll()
                .stream()
                .map(promotionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PromotionResponseDto getPromotionById(Long promotionId) {
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new NotFoundException("Promotion not found with id: " + promotionId));

        return promotionMapper.toDto(promotion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromotionResponseDto> getActivePromotionsByProductId(Long productId) {
        LocalDateTime now = LocalDateTime.now();
        return promotionRepository.findActivePromotionsByProductAndDate(productId, now)
                .stream()
                .map(promotionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromotionResponseDto> getCurrentActivePromotions() {
        LocalDateTime now = LocalDateTime.now();
        return promotionRepository.findActivePromotionsByDate(now)
                .stream()
                .map(promotionMapper::toDto)
                .collect(Collectors.toList());
    }
}
