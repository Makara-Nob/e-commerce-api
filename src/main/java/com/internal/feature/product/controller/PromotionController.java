package com.internal.feature.product.controller;

import com.internal.exceptions.response.ApiResponse;
import com.internal.feature.product.dto.request.PromotionRequestDto;
import com.internal.feature.product.dto.response.PromotionResponseDto;
import com.internal.feature.product.service.PromotionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promotions")
@RequiredArgsConstructor
@Tag(name = "Promotions", description = "Promotion management endpoints")
public class PromotionController {

    private final PromotionService promotionService;

    @PostMapping
    @Operation(summary = "Create a new promotion")
    public ResponseEntity<ApiResponse<PromotionResponseDto>> createPromotion(
            @Valid @RequestBody PromotionRequestDto dto) {

        PromotionResponseDto promotion = promotionService.createPromotion(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Promotion created successfully", promotion));
    }

    @GetMapping
    @Operation(summary = "Get all promotions")
    public ResponseEntity<ApiResponse<List<PromotionResponseDto>>> getAllPromotions(
            @RequestParam(required = false, defaultValue = "false") boolean activeOnly) {

        List<PromotionResponseDto> promotions = activeOnly
                ? promotionService.getCurrentActivePromotions()
                : promotionService.getAllPromotions();

        return ResponseEntity.ok(ApiResponse.success("Promotions retrieved successfully", promotions));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a specific promotion by ID")
    public ResponseEntity<ApiResponse<PromotionResponseDto>> getPromotionById(
            @PathVariable Long id) {

        PromotionResponseDto promotion = promotionService.getPromotionById(id);

        return ResponseEntity.ok(ApiResponse.success("Promotion retrieved successfully", promotion));
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get active promotions for a specific product")
    public ResponseEntity<ApiResponse<List<PromotionResponseDto>>> getPromotionsByProduct(
            @PathVariable Long productId) {

        List<PromotionResponseDto> promotions = promotionService.getActivePromotionsByProductId(productId);

        return ResponseEntity.ok(ApiResponse.success("Product promotions retrieved successfully", promotions));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a promotion")
    public ResponseEntity<ApiResponse<PromotionResponseDto>> updatePromotion(
            @PathVariable Long id,
            @Valid @RequestBody PromotionRequestDto dto) {

        PromotionResponseDto promotion = promotionService.updatePromotion(id, dto);

        return ResponseEntity.ok(ApiResponse.success("Promotion updated successfully", promotion));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a promotion (soft delete)")
    public ResponseEntity<ApiResponse<Void>> deletePromotion(@PathVariable Long id) {

        promotionService.deletePromotion(id);

        return ResponseEntity.ok(ApiResponse.success("Promotion deleted successfully", null));
    }
}
