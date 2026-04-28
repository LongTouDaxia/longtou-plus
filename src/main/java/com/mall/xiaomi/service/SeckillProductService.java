package com.mall.xiaomi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.xiaomi.entity.SeckillProduct;
import com.mall.xiaomi.entity.SeckillTime;
import com.mall.xiaomi.vo.SeckillProductVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SeckillProductService extends IService<SeckillProduct> {
    List<SeckillProductVo> getProduct(String timeId);

    SeckillProductVo getSeckill(String seckillId);

    List<SeckillTime> getTime();

    void addSeckillProduct(SeckillProduct seckillProduct);

    void seckillProduct(String seckillId, Integer userId);
}
