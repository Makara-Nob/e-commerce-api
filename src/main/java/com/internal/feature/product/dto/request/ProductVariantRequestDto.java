package com.internal.feature.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantRequestDto {

    @NotBlank(message = "Variant name is required")
    private String variantName;

    @NotBlank(message = "SKU is required")
    private String sku;

    private String size;

    private String color;

    @NotNull(message = "Stock quantity is required")
    private Integer stockQuantity;

    private BigDecimal additionalPrice;

    private String imageUrl;

    private String status;
}
