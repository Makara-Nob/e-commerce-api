package com.internal.feature.product.controller;

import com.internal.exceptions.response.ApiResponse;
import com.internal.feature.product.dto.request.ProductVariantRequestDto;
import com.internal.feature.product.dto.response.ProductVariantResponseDto;
import com.internal.feature.product.service.ProductVariantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Product Variants", description = "Product variant management endpoints")
public class ProductVariantController {

        private final ProductVariantService productVariantService;

        @PostMapping("/{productId}/variants")
        @Operation(summary = "Create a new product variant")
        public ResponseEntity<ApiResponse<ProductVariantResponseDto>> createVariant(
                        @PathVariable Long productId,
                        @Valid @RequestBody ProductVariantRequestDto dto) {

                ProductVariantResponseDto variant = productVariantService.createVariant(productId, dto);

                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(ApiResponse.success("Product variant created successfully", variant));
        }

        @GetMapping("/{productId}/variants")
        @Operation(summary = "Get all variants for a product")
        public ResponseEntity<ApiResponse<List<ProductVariantResponseDto>>> getVariantsByProduct(
                        @PathVariable Long productId,
                        @RequestParam(required = false, defaultValue = "false") boolean activeOnly) {

                List<ProductVariantResponseDto> variants = activeOnly
                                ? productVariantService.getActiveVariantsByProductId(productId)
                                : productVariantService.getVariantsByProductId(productId);

                return ResponseEntity.ok(ApiResponse.success("Product variants retrieved successfully", variants));
        }

        @GetMapping("/variants/{variantId}")
        @Operation(summary = "Get a specific product variant by ID")
        public ResponseEntity<ApiResponse<ProductVariantResponseDto>> getVariantById(
                        @PathVariable Long variantId) {

                ProductVariantResponseDto variant = productVariantService.getVariantById(variantId);

                return ResponseEntity.ok(ApiResponse.success("Product variant retrieved successfully", variant));
        }

        @PutMapping("/variants/{variantId}")
        @Operation(summary = "Update a product variant")
        public ResponseEntity<ApiResponse<ProductVariantResponseDto>> updateVariant(
                        @PathVariable Long variantId,
                        @Valid @RequestBody ProductVariantRequestDto dto) {

                ProductVariantResponseDto variant = productVariantService.updateVariant(variantId, dto);

                return ResponseEntity.ok(ApiResponse.success("Product variant updated successfully", variant));
        }

        @DeleteMapping("/variants/{variantId}")
        @Operation(summary = "Delete a product variant (soft delete)")
        public ResponseEntity<ApiResponse<Void>> deleteVariant(@PathVariable Long variantId) {

                productVariantService.deleteVariant(variantId);

                return ResponseEntity.ok(ApiResponse.success("Product variant deleted successfully", null));
        }
}
