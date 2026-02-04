package com.internal.feature.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
public class WeeklyStockMovementDto {
    private List<DailyMovement> days;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailyMovement {
        private String dayName;
        private double stockIn;
        private double stockOut;
    }
}
