package com.mall.xiaomi.exception;

import com.mall.xiaomi.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Auther: wdd
 * @Date: 2020-03-19 16:45
 * @Description:
 */
@ControllerAdvice
@ResponseBody
public class XmExceptionHandler {

    @Autowired
    private Result result;

    @ExceptionHandler(XmException.class)
    public Result handleException(XmException e){
        ExceptionEnum em = e.getExceptionEnum();
        result.fail(em.getCode() + "", em.getMsg());
        return result;
    }
}
