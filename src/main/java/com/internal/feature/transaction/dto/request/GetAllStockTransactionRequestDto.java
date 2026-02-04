package com.internal.feature.transaction.dto.request;

import com.internal.enumation.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class GetAllStockTransactionRequestDto {
    
    @Builder.Default
    private int pageNo = 1;
    
    @Builder.Default
    private int pageSize = 10;
    
    private String search;
    private Long productId;
    private TransactionType type;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}