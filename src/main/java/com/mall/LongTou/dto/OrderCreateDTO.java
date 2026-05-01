package com.mall.LongTou.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class OrderCreateDTO {
    @NotNull(message = "用户ID不能为空")
    private Integer userId;

    @NotNull(message = "商品ID不能为空")
    private Integer productId;

    @Min(value = 1, message = "数量至少为1")
    private Integer num;

    // 可选地址ID等
}