package com.internal.feature.product.dto.request;

import com.internal.enumation.StatusData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GetAllProductRequestDto {
    @Builder.Default
    private int pageNo = 1;

    @Builder.Default
    private int pageSize = 10;

    private String search;
    private StatusData status;
    private Long categoryId;
    private Long brandId;
    private java.math.BigDecimal minPrice;
    private java.math.BigDecimal maxPrice;
}
