package com.internal.feature.report.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardStatsDto {
    private long totalProducts;
    private long totalStock;
    private long lowStockCount;
    private long outOfStockCount;
    private long todayStockIn;
    private long todayStockOut;
}
