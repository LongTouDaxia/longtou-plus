package com.mall.LongTou.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("orders")
public class Orders {

         // 订单号（主键）
    private String orderId;

    private Integer userId;          // 用户ID

    private Integer productId;       // 商品ID

    private Integer productNum;      // 购买数量

    private Double productPrice;     // 商品原价（快照）

    private LocalDateTime orderTime;          // 下单时间（毫秒时间戳）

    private Integer orderStatus;     // 订单状态：0-待支付，1-已支付，2-已取消，3-已关闭

    private Long payTime;            // 支付时间（毫秒时间戳，可为空）

    private Integer seckillGoodsId;  // 秒杀商品ID（普通订单为NULL）

    private Double seckillPriceSnapshot; // 秒杀价格快照（普通订单为NULL）

    private String productNameSnapshot;  // 商品名称快照（下单时的商品名）

    // 可选字段：订单总金额（冗余，避免每次计算）
    // private Double orderAmount;
}