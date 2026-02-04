package com.internal.feature.product.service.impl;

import com.internal.enumation.StatusData;
import com.internal.exceptions.error.NotFoundException;
import com.internal.feature.product.dto.request.ProductVariantRequestDto;
import com.internal.feature.product.dto.response.ProductVariantResponseDto;
import com.internal.feature.product.mapper.ProductVariantMapper;
import com.internal.feature.product.model.Product;
import com.internal.feature.product.model.ProductVariant;
import com.internal.feature.product.repository.ProductRepository;
import com.internal.feature.product.repository.ProductVariantRepository;
import com.internal.feature.product.service.ProductVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductVariantServiceImpl implements ProductVariantService {

    private final ProductVariantRepository productVariantRepository;
    private final ProductRepository productRepository;
    private final ProductVariantMapper productVariantMapper;

    @Override
    @Transactional
    public ProductVariantResponseDto createVariant(Long productId, ProductVariantRequestDto dto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found with id: " + productId));

        ProductVariant variant = productVariantMapper.toEntity(dto);
        variant.setProduct(product);

        ProductVariant savedVariant = productVariantRepository.save(variant);
        return productVariantMapper.toDto(savedVariant);
    }

    @Override
    @Transactional
    public ProductVariantResponseDto updateVariant(Long variantId, ProductVariantRequestDto dto) {
        ProductVariant variant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> new NotFoundException("Product variant not found with id: " + variantId));

        productVariantMapper.updateEntityFromDto(dto, variant);

        ProductVariant updatedVariant = productVariantRepository.save(variant);
        return productVariantMapper.toDto(updatedVariant);
    }

    @Override
    @Transactional
    public void deleteVariant(Long variantId) {
        ProductVariant variant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> new NotFoundException("Product variant not found with id: " + variantId));

        variant.setStatus(StatusData.DELETE);
        productVariantRepository.save(variant);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductVariantResponseDto> getVariantsByProductId(Long productId) {
        return productVariantRepository.findByProductId(productId)
                .stream()
                .map(productVariantMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductVariantResponseDto getVariantById(Long variantId) {
        ProductVariant variant = productVariantRepository.findById(variantId)
                .orElseThrow(() -> new NotFoundException("Product variant not found with id: " + variantId));

        return productVariantMapper.toDto(variant);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductVariantResponseDto> getActiveVariantsByProductId(Long productId) {
        return productVariantRepository.findByProductIdAndStatus(productId, StatusData.ACTIVE)
                .stream()
                .map(productVariantMapper::toDto)
                .collect(Collectors.toList());
    }
}
