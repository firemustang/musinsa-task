package com.musinsa.api_task.api.dto.response;

import java.util.List;

public record BrandResponse(Long id, String name, List<ProductPriceResponse> products) {
}
