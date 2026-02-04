package com.internal.feature.product.service;


import com.internal.feature.product.dto.request.CreateProductRequestDto;
import com.internal.feature.product.dto.request.GetAllProductRequestDto;
import com.internal.feature.product.dto.request.UpdateProductRequestDto;
import com.internal.feature.product.dto.response.AllProductResponseDto;
import com.internal.feature.product.dto.response.ProductResponseDto;

public interface ProductService {
    AllProductResponseDto getAllProducts(GetAllProductRequestDto requestDto);

    ProductResponseDto getProductById(Long id);

    ProductResponseDto createProduct(CreateProductRequestDto requestDto);

    ProductResponseDto updateProduct(Long id, UpdateProductRequestDto requestDto);

    ProductResponseDto deleteProduct(Long id);
}

