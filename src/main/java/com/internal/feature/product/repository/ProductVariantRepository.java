package com.internal.feature.product.repository;

import com.internal.enumation.StatusData;
import com.internal.feature.product.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {

    List<ProductVariant> findByProductId(Long productId);

    Optional<ProductVariant> findBySkuAndStatus(String sku, StatusData status);

    List<ProductVariant> findByProductIdAndStatus(Long productId, StatusData status);
}
