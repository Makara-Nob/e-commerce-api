package com.internal.feature.product.dto.response;

import com.internal.enumation.StatusData;
import com.internal.feature.inventory.dto.response.CategoryDTO;
import com.internal.feature.supplier.dto.response.SupplierResponseDto;
import com.internal.feature.product.dto.response.ProductVariantResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDto {
    private Long id;
    private String name;
    private String sku;
    private String description;
    private CategoryDTO category;
    private SupplierResponseDto supplier;
    private BrandResponseDto brand;
    private Integer quantity;
    private Integer minStock;
    private BigDecimal costPrice;
    private BigDecimal sellingPrice;
    private StatusData status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private java.util.List<ProductVariantResponseDto> variants;
}
