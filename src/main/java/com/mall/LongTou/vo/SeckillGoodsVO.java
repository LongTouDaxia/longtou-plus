package com.mall.LongTou.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SeckillGoodsVO {
    private Integer seckillGoodsId;
    private Integer activityId;
    private Integer productId;
    private String productName;        // 商品名称快照或关联查询
    private String productPicture;     // 商品主图
    private BigDecimal seckillPrice;
    private Integer seckillStock;      // 剩余库存
    private Integer limitPerUser;
    private Integer version;           // 乐观锁版本号
}