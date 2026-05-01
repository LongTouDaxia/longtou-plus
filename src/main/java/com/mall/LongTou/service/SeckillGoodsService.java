package com.mall.LongTou.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.LongTou.entity.SeckillGoods;
import com.mall.LongTou.vo.SeckillGoodsVO;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

public interface SeckillGoodsService extends IService<SeckillGoods> {
    // 乐观锁扣减库存
    boolean decreaseStock(Integer seckillGoodsId, Integer quantity, Integer version);
    // 根据活动ID查询商品
    List<SeckillGoodsVO> getByActivityId(Integer activityId);

    void createSeckillOrder(@NotNull(message = "用户ID不能为空") Integer userId, @NotNull(message = "秒杀商品ID不能为空") Integer seckillGoodsId, @Min(value = 1, message = "购买数量至少1件") @Max(value = 10, message = "每场秒杀最多购买10件") Integer quantity);
}