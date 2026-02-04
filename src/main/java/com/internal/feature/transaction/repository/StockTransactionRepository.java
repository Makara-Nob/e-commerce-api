package com.internal.feature.transaction.repository;

import com.internal.feature.transaction.model.StockTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockTransactionRepository extends JpaRepository<StockTransaction, Long>,
                JpaSpecificationExecutor<StockTransaction> {

        List<StockTransaction> findByProductId(Long productId);

        List<StockTransaction> findByTransactionDateBetween(LocalDateTime start, LocalDateTime end);

        List<StockTransaction> findByTransactionDateBetweenOrderByTransactionDateAsc(LocalDateTime start,
                        LocalDateTime end);

        @Query("SELECT SUM(t.quantity) FROM StockTransaction t WHERE t.type = :type AND t.transactionDate BETWEEN :start AND :end")
        Integer sumQuantityByTypeAndTransactionDateBetween(@Param("type") com.internal.enumation.TransactionType type,
                        @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

        @Query("SELECT t FROM StockTransaction t WHERE t.product.id = :productId " +
                        "ORDER BY t.transactionDate DESC")
        List<StockTransaction> findRecentByProductId(@Param("productId") Long productId);
}