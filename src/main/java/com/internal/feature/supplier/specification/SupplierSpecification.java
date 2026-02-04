package com.internal.feature.supplier.specification;

import com.internal.enumation.StatusData;
import com.internal.feature.supplier.model.Supplier;
import org.springframework.data.jpa.domain.Specification;

public class SupplierSpecification {
    public static Specification<Supplier> hasStatus(StatusData status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    public static Specification<Supplier> search(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isBlank()) {
                return null;
            }

            String lowerKeyword = "%" + keyword.toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), lowerKeyword),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("contactPerson")), lowerKeyword),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), lowerKeyword),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("phone")), lowerKeyword),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("address")), lowerKeyword)
            );
        };
    }
}

