package com.mall.xiaomi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName("product")
public class Product {
        @TableId(type = IdType.AUTO)
        private Integer productId;

        private String productName;

        private Integer categoryId;

        private String productTitle;

        private String productPicture;

        private Double productPrice;

        private Double productSellingPrice;

        private Integer productNum;

        private Integer productSales;

        private String productIntro;

        private Integer version;
}
