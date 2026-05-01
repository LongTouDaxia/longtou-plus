package com.mall.LongTou.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;


@Data
@TableName("carousel")
public class Carousel {
    @TableId(type = IdType.AUTO)
    @TableField("carousel_id")
    private Integer carouselId;

    @TableField("img_path")
    private String imgPath;

    @TableField("describes")
    private String describes;

}