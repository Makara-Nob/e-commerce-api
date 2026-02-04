package com.internal.feature.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateProductRequestDto {
    @NotBlank(message = "Product name is required")
    private String name;

    @NotBlank(message = "SKU is required")
    private String sku;

    private String description;

    private Long categoryId;

    private Long supplierId;

    @PositiveOrZero(message = "Quantity must be positive or zero")
    private Integer quantity = 0;

    @PositiveOrZero(message = "Minimum stock must be positive or zero")
    private Integer minStock = 5;

    @PositiveOrZero(message = "Cost price must be positive or zero")
    private BigDecimal costPrice;

    @PositiveOrZero(message = "Selling price must be positive or zero")
    private BigDecimal sellingPrice;
}

