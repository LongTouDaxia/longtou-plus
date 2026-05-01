package com.mall.LongTou.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.LongTou.entity.SeckillActivity;
import com.mall.LongTou.vo.SeckillActivityVO;

import java.util.List;

public interface SeckillActivityService extends IService<SeckillActivity> {
    // 可添加自定义方法，如查询当前有效活动
    List<SeckillActivityVO> getCurrentActivities();
}