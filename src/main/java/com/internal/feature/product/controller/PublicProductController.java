package com.internal.feature.product.controller;

import com.internal.exceptions.response.ApiResponse;
import com.internal.feature.product.dto.request.GetAllProductRequestDto;
import com.internal.feature.product.dto.response.AllProductResponseDto;
import com.internal.feature.product.dto.response.ProductResponseDto;
import com.internal.feature.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/public/products")
@RequiredArgsConstructor
public class PublicProductController {

    private final ProductService productService;

    /**
     * Public endpoint to get all products with pagination and filtering
     * No authentication required
     */
    @PostMapping("/all")
    public ResponseEntity<ApiResponse<AllProductResponseDto>> getAllProducts(
            @RequestBody GetAllProductRequestDto requestDto) {
        return ResponseEntity.ok(
                ApiResponse.success("Products fetched successfully", productService.getAllProducts(requestDto)));
    }

    /**
     * Public endpoint to get a single product by ID
     * No authentication required
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> getProductById(@PathVariable Long id) {
        ProductResponseDto responseDTO = productService.getProductById(id);
        return ResponseEntity.ok(
                ApiResponse.success("Product retrieved successfully", responseDTO));
    }
}
