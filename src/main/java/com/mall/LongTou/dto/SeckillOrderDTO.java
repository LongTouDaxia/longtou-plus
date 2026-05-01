package com.mall.LongTou.dto;

import lombok.Data;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class SeckillOrderDTO {
    @NotNull(message = "用户ID不能为空")
    private Integer userId;

    @NotNull(message = "秒杀商品ID不能为空")
    private Integer seckillGoodsId;

    @Min(value = 1, message = "购买数量至少1件")
    @Max(value = 10, message = "每场秒杀最多购买10件")
    private Integer quantity = 1;
}