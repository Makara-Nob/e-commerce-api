package com.internal.feature.product.repository;

import com.internal.feature.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>,
        JpaSpecificationExecutor<Product> {

    Optional<Product> findBySku(String sku);

    boolean existsBySku(String sku);

    boolean existsBySkuAndIdNot(String sku, Long id);

    long countByStatus(com.internal.enumation.StatusData status);

    long countByQuantity(Integer quantity);

    long countByQuantityLessThanEqual(Integer quantity);

    @org.springframework.data.jpa.repository.Query("SELECT SUM(p.quantity) FROM Product p")
    Integer sumQuantity();
}
