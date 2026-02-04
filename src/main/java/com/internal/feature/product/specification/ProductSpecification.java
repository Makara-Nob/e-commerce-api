package com.internal.feature.product.specification;

import com.internal.enumation.StatusData;
import com.internal.feature.product.model.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification {
    public static Specification<Product> hasStatus(StatusData status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    public static Specification<Product> search(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isBlank()) {
                return null;
            }

            String lowerKeyword = "%" + keyword.toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), lowerKeyword),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("sku")), lowerKeyword),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("category")), lowerKeyword),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), lowerKeyword)
            );
        };
    }
}

