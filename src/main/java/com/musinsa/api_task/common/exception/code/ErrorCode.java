package com.musinsa.api_task.common.exception.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    NO_BRANDS_FOUND(HttpStatus.BAD_REQUEST, "브랜드를 찾을 수 없습니다."),
    NO_PRODUCTS_FOUND(HttpStatus.BAD_REQUEST, "상품을 찾을 수 없습니다."),
    INVALID_CATEGORY(HttpStatus.BAD_REQUEST, "카테고리 값이 유효하지 않습니다"),
    INTERNAL_SERVER_ERROR(HttpStatus.BAD_REQUEST, "내부 서버 에러 입니다."),

    ;
    private final HttpStatus httpStatus;
    private final String message;

}
