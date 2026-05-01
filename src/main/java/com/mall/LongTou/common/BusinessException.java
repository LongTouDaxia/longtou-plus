package com.mall.LongTou.common;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ExceptionEnum exceptionEnum;

    public BusinessException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMessage());
        this.exceptionEnum = exceptionEnum;
    }

    public BusinessException(ExceptionEnum exceptionEnum, String message) {
        super(message);
        this.exceptionEnum = exceptionEnum;
    }
}