package com.musinsa.api_task.api.dto.response;

import java.util.Map;

public record BrandLowestPriceResponse (Map<String, ProductPriceResponse> lowestPrices, int totalAmount) {
}