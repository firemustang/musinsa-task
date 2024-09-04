package com.musinsa.api_task.domain.repository;

import com.musinsa.api_task.domain.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    @Query("SELECT b FROM Brand b LEFT JOIN FETCH b.productPrices")
    List<Brand> findAllWithProductPrices();

    Optional<Brand> findByName(String name);
    @Query("SELECT b FROM Brand b LEFT JOIN FETCH b.productPrices")
    Page<Brand> findAllWithProductPrices(Pageable pageable);
}
