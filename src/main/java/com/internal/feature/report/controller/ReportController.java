package com.internal.feature.report.controller;

import com.internal.feature.report.dto.DashboardStatsDto;
import com.internal.feature.report.dto.LowStockAlertDto;
import com.internal.feature.report.dto.StockStatusDistributionDto;
import com.internal.feature.report.dto.WeeklyStockMovementDto;
import com.internal.feature.report.service.ReportService;
import com.internal.exceptions.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@Tag(name = "Report Management", description = "Endpoints for Dashboard Statistics and Reports")
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "Get Dashboard Statistics", description = "Returns aggregated counts for total products, stock, low stock, etc.")
    @GetMapping("/dashboard-stats")
    public ResponseEntity<ApiResponse<DashboardStatsDto>> getDashboardStats() {
        return ResponseEntity
                .ok(ApiResponse.success("Dashboard stats retrieved successfully", reportService.getDashboardStats()));
    }

    @Operation(summary = "Get Stock Status Distribution", description = "Returns percentage distribution for stock statuses (for Pie Chart)")
    @GetMapping("/stock-distribution")
    public ResponseEntity<ApiResponse<StockStatusDistributionDto>> getStockStatusDistribution() {
        return ResponseEntity.ok(ApiResponse.success("Stock status distribution retrieved successfully",
                reportService.getStockStatusDistribution()));
    }

    @Operation(summary = "Get Weekly Stock Movements", description = "Returns daily stock in/out summations for the last 7 days (for Bar Chart)")
    @GetMapping("/weekly-movements")
    public ResponseEntity<ApiResponse<WeeklyStockMovementDto>> getWeeklyStockMovements() {
        return ResponseEntity.ok(ApiResponse.success("Weekly stock movements retrieved successfully",
                reportService.getWeeklyStockMovements()));
    }

    @Operation(summary = "Get Low Stock Alerts", description = "Returns list of products with low stock or out of stock status")
    @GetMapping("/alerts")
    public ResponseEntity<ApiResponse<List<LowStockAlertDto>>> getLowStockAlerts() {
        return ResponseEntity
                .ok(ApiResponse.success("Low stock alerts retrieved successfully", reportService.getLowStockAlerts()));
    }
}
