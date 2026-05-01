package com.mall.LongTou.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SeckillActivityVO {
    private Integer activityId;
    private String activityName;
    private LocalDateTime startTime;   // 或者用 Long 时间戳，根据实体定义
    private LocalDateTime endTime;
    private Integer status;
    private LocalDateTime createTime;
}