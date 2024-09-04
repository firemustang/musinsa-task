package com.musinsa.api_task.domain.dto;

import java.util.Map;

public record BrandDto(String name, Map<String, Integer> productPrices) {
}
