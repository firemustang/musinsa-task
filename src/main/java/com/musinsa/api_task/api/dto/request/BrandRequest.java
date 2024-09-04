package com.musinsa.api_task.api.dto.request;

import java.util.List;

public record BrandRequest(String name, List<ProductPriceRequest> products) {
}
