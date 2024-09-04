package com.musinsa.api_task.domain.repository;

import com.musinsa.api_task.domain.entity.ProductPrice;
import com.musinsa.api_task.domain.enums.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long> {
    List<ProductPrice> findByBrandIdOrderByCategoryAsc(Long brandId);
    List<ProductPrice> findByCategory(Category category);

}
