package com.musinsa.api_task.api.dto.response;

import com.musinsa.api_task.domain.dto.PriceInfoDto;
import com.musinsa.api_task.domain.enums.Category;

public record PriceInfoResponse(String category, PriceInfoDto lowestPrice, PriceInfoDto highestPrice) {
}
