package com.internal.feature.product.controller;

import com.internal.exceptions.response.ApiResponse;
import com.internal.feature.product.dto.response.PromotionResponseDto;
import com.internal.feature.product.service.PromotionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/public/promotions")
@RequiredArgsConstructor
@Tag(name = "Public Promotions", description = "Public promotion endpoints for customers")
public class PublicPromotionController {

    private final PromotionService promotionService;

    @GetMapping
    @Operation(summary = "Get all active promotions")
    public ResponseEntity<ApiResponse<List<PromotionResponseDto>>> getActivePromotions() {
        List<PromotionResponseDto> promotions = promotionService.getCurrentActivePromotions();
        return ResponseEntity.ok(ApiResponse.success("Active promotions retrieved successfully", promotions));
    }
}
