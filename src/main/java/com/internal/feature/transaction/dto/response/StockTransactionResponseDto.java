package com.internal.feature.transaction.dto.response;

import com.internal.feature.product.dto.response.ProductResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockTransactionResponseDto {
    private Long id;
    private ProductResponseDto product;
    private String type;
    private Integer quantity;
    private Integer previousStock;
    private Integer newStock;
    private String reference;
    private String notes;
    private LocalDateTime transactionDate;
    private LocalDateTime createdAt;
    private String createdBy;
}