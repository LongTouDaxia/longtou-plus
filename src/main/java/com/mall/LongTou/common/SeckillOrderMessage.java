package com.mall.LongTou.common;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SeckillOrderMessage implements Serializable {
    private Integer userId;
    private Integer seckillGoodsId;
    private Integer quantity;

    private String orderToken;  //返回前端唯一订单id
}
