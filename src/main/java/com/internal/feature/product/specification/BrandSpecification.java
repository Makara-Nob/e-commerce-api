package com.internal.feature.product.specification;

import com.internal.feature.product.model.Brand;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class BrandSpecification {

    public static Specification<Brand> search(String searchTerm) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(searchTerm)) {
                return cb.conjunction();
            }
            String searchPattern = "%" + searchTerm.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("name")), searchPattern),
                    cb.like(cb.lower(root.get("description")), searchPattern));
        };
    }
}
