package com.mall.LongTou.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("seckill_activity")
public class SeckillActivity {
    @TableId(type = IdType.AUTO)
    private Integer activityId;

    private String activityName;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer status;   // 1-启用 0-停用

    private LocalDateTime createTime;
}