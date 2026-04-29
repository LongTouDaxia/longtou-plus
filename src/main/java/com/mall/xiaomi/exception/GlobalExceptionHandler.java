package com.mall.xiaomi.exception;

import com.mall.xiaomi.util.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ExceptionHandler;
/**
 * @Auther: wdd
 * @Date: 2020-03-19 16:45
 * @Description:
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {



    @ExceptionHandler(Exception.class)
    public Result handleException(XmException e){
        ExceptionEnum em = e.getExceptionEnum();
        return Result.error(em.getCode() + "", em.getMsg());
    }
}
