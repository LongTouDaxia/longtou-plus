package com.mall.xiaomi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Auther: wdd
 * @Date: 2020-03-28 19:40
 * @Description:
 */
@Data
@TableName("seckill_product")
public class SeckillProduct implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer seckillId;

    private Integer productId;

    private Double seckillPrice;

    private Integer seckillStock;

    private Integer timeId;
}
