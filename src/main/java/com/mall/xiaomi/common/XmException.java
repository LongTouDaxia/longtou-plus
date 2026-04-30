package com.mall.xiaomi.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Auther: wdd
 * @Date: 2020-03-19 16:43
 * @Description:
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class XmException extends RuntimeException{
    private ExceptionEnum exceptionEnum;
}
