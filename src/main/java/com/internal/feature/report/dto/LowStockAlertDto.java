package com.internal.feature.report.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LowStockAlertDto {
    private String productName;
    private String sku;
    private int quantity;
    private String statusLabel; // "Low Stock" or "Out of Stock"
    private int minStock;
}
