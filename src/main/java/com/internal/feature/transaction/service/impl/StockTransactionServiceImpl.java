package com.internal.feature.transaction.service.impl;

import com.internal.enumation.TransactionType;
import com.internal.exceptions.error.BadRequestException;
import com.internal.exceptions.error.NotFoundException;
import com.internal.feature.product.model.Product;
import com.internal.feature.product.repository.ProductRepository;
import com.internal.feature.transaction.dto.request.CreateStockTransactionRequestDto;
import com.internal.feature.transaction.dto.request.GetAllStockTransactionRequestDto;
import com.internal.feature.transaction.dto.response.AllStockTransactionResponseDto;
import com.internal.feature.transaction.dto.response.StockTransactionResponseDto;
import com.internal.feature.transaction.mapper.StockTransactionMapper;
import com.internal.feature.transaction.model.StockTransaction;
import com.internal.feature.transaction.repository.StockTransactionRepository;
import com.internal.feature.transaction.service.StockTransactionService;
import com.internal.feature.transaction.specification.StockTransactionSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class StockTransactionServiceImpl implements StockTransactionService {

    private final StockTransactionRepository transactionRepository;
    private final ProductRepository productRepository;
    private final StockTransactionMapper transactionMapper;

    @Override
    @Transactional(readOnly = true)
    public AllStockTransactionResponseDto getAllTransactions(GetAllStockTransactionRequestDto requestDto) {
        log.debug("Fetching all stock transactions with filter: {}", requestDto);

        Specification<StockTransaction> spec = Specification
                .where(StockTransactionSpecification.search(requestDto.getSearch()))
                .and(StockTransactionSpecification.hasProductId(requestDto.getProductId()))
                .and(StockTransactionSpecification.hasType(requestDto.getType()))
                .and(StockTransactionSpecification.betweenDates(requestDto.getStartDate(), requestDto.getEndDate()));

        Pageable pageable = PageRequest.of(requestDto.getPageNo() - 1, requestDto.getPageSize(),
                Sort.by(Sort.Direction.DESC, "transactionDate"));

        Page<StockTransaction> transactions = transactionRepository.findAll(spec, pageable);

        log.info("Fetched {} transactions (page {} of {})",
                transactions.getNumberOfElements(), transactions.getNumber() + 1, transactions.getTotalPages());

        List<StockTransactionResponseDto> transactionDTOList = transactionMapper.mapToListDto(transactions);
        return transactionMapper.mapToPaginateDto(transactionDTOList, transactions);
    }

    @Override
    @Transactional(readOnly = true)
    public StockTransactionResponseDto getTransactionById(Long id) {
        log.debug("Fetching stock transaction by ID: {}", id);

        StockTransaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Stock transaction not found with ID: {}", id);
                    return new NotFoundException("Stock transaction not found with ID: " + id);
                });

        log.debug("Fetched transaction: id={}, type='{}'", transaction.getId(), transaction.getType());
        return transactionMapper.toDto(transaction);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StockTransactionResponseDto> getTransactionsByProductId(Long productId) {
        log.debug("Fetching stock transactions for product ID: {}", productId);

        // Verify product exists
        if (!productRepository.existsById(productId)) {
            throw new NotFoundException("Product not found with ID: " + productId);
        }

        List<StockTransaction> transactions = transactionRepository.findRecentByProductId(productId);
        
        log.info("Found {} transactions for product ID: {}", transactions.size(), productId);
        
        return transactions.stream()
                .map(transactionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public StockTransactionResponseDto createTransaction(CreateStockTransactionRequestDto requestDto) {
        log.info("Request to create stock transaction: productId={}, type={}, quantity={}",
                requestDto.getProductId(), requestDto.getType(), requestDto.getQuantity());

        try {
            // Fetch product
            Product product = productRepository.findById(requestDto.getProductId())
                    .orElseThrow(() -> {
                        log.warn("Product not found with ID: {}", requestDto.getProductId());
                        return new NotFoundException("Product not found with ID: " + requestDto.getProductId());
                    });

            // Validate transaction
            validateTransaction(requestDto, product);

            // Create transaction
            StockTransaction transaction = transactionMapper.toEntity(requestDto);
            transaction.setProduct(product);
            transaction.setPreviousStock(product.getQuantity());

            // Update product stock based on transaction type
            int newStock = calculateNewStock(product.getQuantity(), requestDto.getQuantity(), requestDto.getType());
            transaction.setNewStock(newStock);

            // Update product quantity
            product.setQuantity(newStock);
            productRepository.save(product);

            // Save transaction
            StockTransaction savedTransaction = transactionRepository.save(transaction);

            log.info("Stock transaction created successfully: id={}, product='{}', previousStock={}, newStock={}",
                    savedTransaction.getId(), product.getName(), 
                    savedTransaction.getPreviousStock(), savedTransaction.getNewStock());

            return transactionMapper.toDto(savedTransaction);

        } catch (Exception e) {
            log.error("Error creating stock transaction: productId={}, type={}, message={}",
                    requestDto.getProductId(), requestDto.getType(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public StockTransactionResponseDto deleteTransaction(Long id) {
        log.info("Request to delete stock transaction: id={}", id);

        try {
            StockTransaction transaction = transactionRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("Stock transaction not found for deletion: id={}", id);
                        return new NotFoundException("Stock transaction not found with ID: " + id);
                    });

            // Reverse the stock change
            Product product = transaction.getProduct();
            int reversedStock = reverseStockChange(
                    product.getQuantity(), 
                    transaction.getQuantity(), 
                    transaction.getType()
            );
            product.setQuantity(reversedStock);
            productRepository.save(product);

            transactionRepository.delete(transaction);
            log.info("Stock transaction deleted successfully: id={}, stock reversed from {} to {}",
                    id, product.getQuantity() - reversedStock, reversedStock);

            return transactionMapper.toDto(transaction);

        } catch (Exception e) {
            log.error("Error deleting stock transaction id={}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    private void validateTransaction(CreateStockTransactionRequestDto requestDto, Product product) {
        // For STOCK_OUT, DAMAGED, EXPIRED, etc., ensure sufficient stock
        if (isStockReducingTransaction(requestDto.getType())) {
            if (product.getQuantity() < requestDto.getQuantity()) {
                throw new BadRequestException(
                        "Insufficient stock. Available: " + product.getQuantity() + 
                        ", Requested: " + requestDto.getQuantity()
                );
            }
        }
    }

    private int calculateNewStock(int currentStock, int quantity, TransactionType type) {
        if (isStockIncreasingTransaction(type)) {
            return currentStock + quantity;
        } else if (isStockReducingTransaction(type)) {
            return currentStock - quantity;
        }
        return currentStock; // For other types, no change
    }

    private int reverseStockChange(int currentStock, int quantity, TransactionType type) {
        // Reverse the operation
        if (isStockIncreasingTransaction(type)) {
            return currentStock - quantity;
        } else if (isStockReducingTransaction(type)) {
            return currentStock + quantity;
        }
        return currentStock;
    }

    private boolean isStockIncreasingTransaction(TransactionType type) {
        return type == TransactionType.STOCK_IN || type == TransactionType.RETURN_TO_SUPPLIER;
    }

    private boolean isStockReducingTransaction(TransactionType type) {
        return type == TransactionType.STOCK_OUT ||
               type == TransactionType.DAMAGED ||
               type == TransactionType.EXPIRED ||
               type == TransactionType.SAMPLE ||
               type == TransactionType.PRODUCTION ||
               type == TransactionType.TRANSFER;
    }
}