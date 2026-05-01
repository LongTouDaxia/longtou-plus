package com.mall.LongTou.dto;

import lombok.Data;
import javax.validation.constraints.NotNull;

@Data
public class CollectDTO {
    @NotNull(message = "用户ID不能为空")
    private Integer userId;

    @NotNull(message = "商品ID不能为空")
    private Integer productId;
}