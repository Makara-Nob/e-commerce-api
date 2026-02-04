package com.internal.feature.transaction.controller;

import com.internal.exceptions.response.ApiResponse;
import com.internal.feature.transaction.dto.request.CreateStockTransactionRequestDto;
import com.internal.feature.transaction.dto.request.GetAllStockTransactionRequestDto;
import com.internal.feature.transaction.dto.response.AllStockTransactionResponseDto;
import com.internal.feature.transaction.dto.response.StockTransactionResponseDto;
import com.internal.feature.transaction.service.StockTransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stock-transactions")
@RequiredArgsConstructor
@Tag(name = "Stock Transactions")
public class StockTransactionController {

    private final StockTransactionService transactionService;

    @PostMapping
    public ResponseEntity<ApiResponse<StockTransactionResponseDto>> createTransaction(
            @Valid @RequestBody CreateStockTransactionRequestDto requestDTO) {
        StockTransactionResponseDto responseDTO = transactionService.createTransaction(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Stock transaction created successfully", responseDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StockTransactionResponseDto>> getTransactionById(@PathVariable Long id) {
        StockTransactionResponseDto responseDTO = transactionService.getTransactionById(id);
        return ResponseEntity.ok(ApiResponse.success("Transaction retrieved successfully", responseDTO));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<List<StockTransactionResponseDto>>> getTransactionsByProductId(
            @PathVariable Long productId) {
        List<StockTransactionResponseDto> transactions = transactionService.getTransactionsByProductId(productId);
        return ResponseEntity.ok(ApiResponse.success("Product transactions retrieved successfully", transactions));
    }

    @PostMapping("/all")
    public ResponseEntity<ApiResponse<AllStockTransactionResponseDto>> getAllTransactions(
            @RequestBody GetAllStockTransactionRequestDto requestDto) {
        AllStockTransactionResponseDto response = transactionService.getAllTransactions(requestDto);
        return ResponseEntity.ok(ApiResponse.success("Transactions fetched successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<StockTransactionResponseDto>> deleteTransaction(@PathVariable Long id) {
        StockTransactionResponseDto transactionDTO = transactionService.deleteTransaction(id);
        return ResponseEntity.ok(ApiResponse.success("Transaction deleted and stock reversed successfully", transactionDTO));
    }
}