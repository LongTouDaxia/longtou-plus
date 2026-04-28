package com.mall.xiaomi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName("shopping_cart")
public class ShoppingCart {
        @TableId(type = IdType.AUTO)
        private Integer id;

        private Integer userId;

        private Integer productId;

        private Integer num;

        private Integer version; // 版本号，用于乐观锁控制
}
