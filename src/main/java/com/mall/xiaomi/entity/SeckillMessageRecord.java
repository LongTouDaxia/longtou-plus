package com.mall.xiaomi.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * seckill_message_record
 */
@Data
@TableName("seckill_message_record")
public class SeckillMessageRecord implements Serializable {


    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String messageId;

    private String userId;

    private String seckillId;

    private String status;

    private Integer retryCount;

    private String errorMessage;

    private Date createdAt;

    private Date updatedAt;

}
