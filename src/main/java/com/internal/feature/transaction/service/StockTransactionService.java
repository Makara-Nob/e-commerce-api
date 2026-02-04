package com.internal.feature.transaction.service;

import com.internal.feature.transaction.dto.request.CreateStockTransactionRequestDto;
import com.internal.feature.transaction.dto.request.GetAllStockTransactionRequestDto;
import com.internal.feature.transaction.dto.response.AllStockTransactionResponseDto;
import com.internal.feature.transaction.dto.response.StockTransactionResponseDto;

import java.util.List;

public interface StockTransactionService {
    
    AllStockTransactionResponseDto getAllTransactions(GetAllStockTransactionRequestDto requestDto);
    
    StockTransactionResponseDto getTransactionById(Long id);
    
    List<StockTransactionResponseDto> getTransactionsByProductId(Long productId);
    
    StockTransactionResponseDto createTransaction(CreateStockTransactionRequestDto requestDto);
    
    StockTransactionResponseDto deleteTransaction(Long id);
}