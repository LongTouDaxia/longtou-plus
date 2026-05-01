package com.mall.LongTou.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.LongTou.entity.SeckillProduct;
import com.mall.LongTou.vo.SeckillActivityVO;
import com.mall.LongTou.vo.SeckillGoodsVO;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public interface SeckillService extends IService<SeckillProduct> {

    List<SeckillActivityVO> getCurrentActivities();

    List<SeckillGoodsVO> getGoodsByActivity(@NotNull(message = "活动ID不能为空") Integer activityId);

    void createSeckillOrder(@NotNull(message = "用户ID不能为空") Integer userId, @NotNull(message = "秒杀商品ID不能为空") Integer seckillGoodsId, @Min(value = 1, message = "购买数量至少1件") @Max(value = 10, message = "每场秒杀最多购买10件") Integer quantity);
}
