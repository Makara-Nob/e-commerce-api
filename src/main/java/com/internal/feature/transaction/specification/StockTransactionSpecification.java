package com.internal.feature.transaction.specification;

import com.internal.enumation.TransactionType;
import com.internal.feature.transaction.model.StockTransaction;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class StockTransactionSpecification {
    
    public static Specification<StockTransaction> hasProductId(Long productId) {
        return (root, query, criteriaBuilder) -> {
            if (productId == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("product").get("id"), productId);
        };
    }

    public static Specification<StockTransaction> hasType(TransactionType type) {
        return (root, query, criteriaBuilder) -> {
            if (type == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("type"), type);
        };
    }

    public static Specification<StockTransaction> betweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null && endDate == null) {
                return null;
            }
            if (startDate != null && endDate != null) {
                return criteriaBuilder.between(root.get("transactionDate"), startDate, endDate);
            }
            if (startDate != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("transactionDate"), startDate);
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("transactionDate"), endDate);
        };
    }

    public static Specification<StockTransaction> search(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isBlank()) {
                return null;
            }

            String lowerKeyword = "%" + keyword.toLowerCase() + "%";

            // Join with product for searching
            Join<Object, Object> productJoin = root.join("product", jakarta.persistence.criteria.JoinType.LEFT);

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(productJoin.get("name")), lowerKeyword),
                    criteriaBuilder.like(criteriaBuilder.lower(productJoin.get("sku")), lowerKeyword),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("reference")), lowerKeyword),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("notes")), lowerKeyword)
            );
        };
    }
}