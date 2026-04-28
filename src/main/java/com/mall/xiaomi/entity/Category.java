package com.mall.xiaomi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName( "category")
public class Category {

    @TableId(type = IdType.AUTO)
    private Integer categoryId;

    private String categoryName;

}