package com.mall.LongTou.config;

import com.mall.LongTou.common.BusinessException;
import com.mall.LongTou.common.ExceptionEnum;
import com.mall.LongTou.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 处理 @Valid @RequestBody 校验失败
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : "参数校验失败";
        log.warn("参数校验失败: {}", message);
        return Result.error(400, message);
    }

    // 处理 @Validated 在方法参数上的校验失败（如 @RequestParam @PathVariable）
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Void> handleConstraintViolation(ConstraintViolationException e) {
        ConstraintViolation<?> violation = e.getConstraintViolations().iterator().next();
        String message = violation.getMessage();
        log.warn("参数校验失败: {}", message);
        return Result.error(400, message);
    }

    // 处理表单绑定失败（@Valid 用在普通参数上）
    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : "参数绑定失败";
        log.warn("参数绑定失败: {}", message);
        return Result.error(400, message);
    }

    // 处理业务异常
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        log.warn("业务异常: code={}, message={}", e.getExceptionEnum().getCode(), e.getMessage());
        return Result.error(e.getExceptionEnum());
    }

    // 处理其他未捕获异常
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("系统异常", e);
        return Result.error(ExceptionEnum.SYSTEM_BUSY);
    }
}