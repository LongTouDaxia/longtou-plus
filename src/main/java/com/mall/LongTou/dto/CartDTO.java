package com.mall.LongTou.dto;

import lombok.Data;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class CartDTO {
    @NotNull(message = "用户ID不能为空")
    private Integer userId;

    @NotNull(message = "商品ID不能为空")
    private Integer productId;

    @NotNull(message = "数量不能为空")
    @Min(value = 1, message = "数量至少为1")
    @Max(value = 999, message = "数量不能超过999")
    private Integer num;
}