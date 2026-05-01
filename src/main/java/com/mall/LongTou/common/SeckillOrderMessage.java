package com.mall.LongTou.common;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SeckillOrderMessage implements Serializable {
    private Integer userId;
    private Integer seckillGoodsId;
    private Integer quantity;

}
