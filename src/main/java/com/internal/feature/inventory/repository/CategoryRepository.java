package com.internal.feature.inventory.repository;

import com.internal.feature.inventory.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>,
        JpaSpecificationExecutor<Category> {
    
    Optional<Category> findByName(String name);
    
    Optional<Category> findByCode(String code);
    
    boolean existsByName(String name);
    
    boolean existsByCode(String code);
    
    boolean existsByNameAndIdNot(String name, Long id);
    
    boolean existsByCodeAndIdNot(String code, Long id);
}
