package com.internal.feature.product.dto.request;

import com.internal.enumation.StatusData;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateProductRequestDto {
    private String name;
    private String sku;
    private String description;
    private String category;

    private Long categoryId;

    private Long supplierId;

    @PositiveOrZero(message = "Quantity must be positive or zero")
    private Integer quantity;
    
    @PositiveOrZero(message = "Minimum stock must be positive or zero")
    private Integer minStock;
    
    @PositiveOrZero(message = "Cost price must be positive or zero")
    private BigDecimal costPrice;
    
    @PositiveOrZero(message = "Selling price must be positive or zero")
    private BigDecimal sellingPrice;
    
    private StatusData status;
}
