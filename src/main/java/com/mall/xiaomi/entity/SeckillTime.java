package com.mall.xiaomi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


/**
 * @Auther: wdd
 * @Date: 2020-04-22 20:57
 * @Description:
 */
@Data
@TableName( "seckill_time")
public class SeckillTime {

    @TableId(type = IdType.AUTO)
    private Integer timeId;

    private Long startTime;

    private Long endTime;

}
