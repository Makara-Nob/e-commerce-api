package com.internal.feature.product.controller;

import com.internal.exceptions.response.ApiResponse;
import com.internal.feature.product.dto.request.CreateProductRequestDto;
import com.internal.feature.product.dto.request.GetAllProductRequestDto;
import com.internal.feature.product.dto.request.UpdateProductRequestDto;
import com.internal.feature.product.dto.response.AllProductResponseDto;
import com.internal.feature.product.dto.response.ProductResponseDto;
import com.internal.feature.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // ----------------- CRUD -----------------
    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponseDto>> createProduct(
            @Valid @RequestBody CreateProductRequestDto requestDTO) {
        ProductResponseDto responseDTO = productService.createProduct(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Product created successfully", responseDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequestDto requestDTO) {
        ProductResponseDto responseDTO = productService.updateProduct(id, requestDTO);
        return ResponseEntity.ok(ApiResponse.success("Product updated successfully", responseDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> getProductById(@PathVariable Long id) {
        ProductResponseDto responseDTO = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.success("Product retrieved successfully", responseDTO));
    }

    @PostMapping("/all")
    public ResponseEntity<ApiResponse<AllProductResponseDto>> getAllProducts(@RequestBody GetAllProductRequestDto requestDto) {
        return ResponseEntity.ok(ApiResponse.success("Products fetched successfully", productService.getAllProducts(requestDto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> deleteProduct(@PathVariable Long id) {
        ProductResponseDto productDTO = productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success("Product deleted successfully", productDTO));
    }
}

