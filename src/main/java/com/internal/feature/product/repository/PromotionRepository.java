package com.internal.feature.product.repository;

import com.internal.enumation.StatusData;
import com.internal.feature.product.model.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {

    List<Promotion> findByProductIdAndStatus(Long productId, StatusData status);

    @Query("SELECT p FROM Promotion p WHERE p.status = 'ACTIVE' " +
            "AND p.startDate <= :now AND p.endDate >= :now")
    List<Promotion> findActivePromotionsByDate(@Param("now") LocalDateTime now);

    @Query("SELECT p FROM Promotion p WHERE p.product.id = :productId " +
            "AND p.status = 'ACTIVE' AND p.startDate <= :now AND p.endDate >= :now")
    List<Promotion> findActivePromotionsByProductAndDate(
            @Param("productId") Long productId,
            @Param("now") LocalDateTime now);
}
