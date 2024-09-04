package com.musinsa.api_task.domain.repository;

import com.musinsa.api_task.domain.entity.ProductPrice;
import com.musinsa.api_task.domain.enums.Category;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long> {
    @EntityGraph(attributePaths = {"brand"})
    List<ProductPrice> findAll();

    List<ProductPrice> findByCategory(Category category);

}
