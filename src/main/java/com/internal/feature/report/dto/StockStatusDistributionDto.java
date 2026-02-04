package com.internal.feature.report.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StockStatusDistributionDto {
    private double activePercent;
    private double normalPercent;
    private double lowStockPercent;
    private double outOfStockPercent;
}
