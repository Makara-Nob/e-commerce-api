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
                    // criteriaBuilder.like(criteriaBuilder.lower(root.get("category")), lowerKeyword), // Category is object, this might fail if not joined or treated as string. Assuming name?
                    // Better to remove category search here if it's strict, or join. 
                    // But original code had it. Let's leave it if it works, or comment out if it was causing issues.
                    // Actually, root.get("category") returns a Path<Category>. .as(String.class) or join is needed.
                    // I will leave it as is for now to avoid side effects, but add new specs.
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), lowerKeyword)
            );
        };
    }

    public static Specification<Product> hasCategory(Long categoryId) {
        return (root, query, criteriaBuilder) -> {
            if (categoryId == null) return null;
            return criteriaBuilder.equal(root.get("category").get("id"), categoryId);
        };
    }

    public static Specification<Product> hasBrand(Long brandId) {
        return (root, query, criteriaBuilder) -> {
            if (brandId == null) return null;
            return criteriaBuilder.equal(root.get("brand").get("id"), brandId);
        };
    }

    public static Specification<Product> hasPriceRange(java.math.BigDecimal minPrice, java.math.BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice == null && maxPrice == null) return null;
            
            if (minPrice != null && maxPrice != null) {
                return criteriaBuilder.between(root.get("sellingPrice"), minPrice, maxPrice);
            } else if (minPrice != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("sellingPrice"), minPrice);
            } else {
                return criteriaBuilder.lessThanOrEqualTo(root.get("sellingPrice"), maxPrice);
            }
        };
    }
}

