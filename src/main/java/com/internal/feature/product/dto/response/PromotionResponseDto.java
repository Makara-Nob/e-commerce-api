package com.internal.feature.product.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromotionResponseDto {

    private Long id;
    private String name;
    private String description;
    private String discountType;
    private BigDecimal discountValue;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long productId;
    private String productName;
    private ProductResponseDto product; // Full product details
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
