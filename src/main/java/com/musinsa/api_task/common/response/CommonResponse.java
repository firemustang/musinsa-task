package com.musinsa.api_task.common.response;

import com.musinsa.api_task.common.exception.code.ErrorCode;
import com.musinsa.api_task.common.exception.code.SuccessCode;

public record CommonResponse<T>(boolean success, String message, T result) {

    public static <T> CommonResponse<T> success(SuccessCode successCode, T result) {
        return new CommonResponse<>(true, successCode.getMessage(), result);
    }

    public static <T> CommonResponse<T> fail(ErrorCode errorCode, String message) {
        return new CommonResponse<>(false, errorCode.getMessage() + ": " + message, null);
    }

    public static <T> CommonResponse<T> fail(ErrorCode errorCode) {
        return new CommonResponse<>(false, errorCode.getMessage(), null);
    }

}

