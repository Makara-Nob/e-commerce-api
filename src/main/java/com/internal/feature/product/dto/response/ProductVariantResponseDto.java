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
public class ProductVariantResponseDto {

    private Long id;
    private Long productId;
    private String productName;
    private String variantName;
    private String sku;
    private String size;
    private String color;
    private Integer stockQuantity;
    private BigDecimal additionalPrice;
    private String imageUrl;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
