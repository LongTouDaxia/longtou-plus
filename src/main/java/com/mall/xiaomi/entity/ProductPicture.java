package com.mall.xiaomi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName("product_picture")
public class ProductPicture {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer productId;

    private String productPicture;

    private String intro;

}