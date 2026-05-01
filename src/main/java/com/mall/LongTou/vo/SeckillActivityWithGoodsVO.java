package com.mall.LongTou.vo;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class SeckillActivityWithGoodsVO {
    private Integer activityId;
    private String activityName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer status;
    private List<SeckillGoodsVO> goodsList;
}