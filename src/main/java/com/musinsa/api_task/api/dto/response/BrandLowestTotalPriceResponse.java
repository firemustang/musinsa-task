package com.musinsa.api_task.api.dto.response;

import com.musinsa.api_task.domain.dto.CategoryPriceDto;

import java.util.List;

public record BrandLowestTotalPriceResponse(String brandName, List<CategoryPriceDto> categories, int totalPrice) {
}
