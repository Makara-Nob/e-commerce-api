package com.internal.feature.product.dto.request;

import lombok.Data;

@Data
public class AllBrandRequestDto {
    private int page;
    private int size;
    private String searchTerm;
}
