package com.musinsa.api_task.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Entity
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductPrice> productPrices = new ArrayList<>(); // 초기화

    public Brand(String name) {
        this.name = name;
        this.productPrices = new ArrayList<>(); // 초기화
    }

    public List<ProductPrice> getProductPrices() {
        return Collections.unmodifiableList(productPrices); // 불변 리스트 반환
    }

    public void setProductPrices(List<ProductPrice> productPrices) {
        this.productPrices.clear(); // 기존 리스트를 비우고
        this.productPrices.addAll(productPrices); // 새로운 리스트 추가
    }
}
