package com.mall.xiaomi.controller;

import com.mall.xiaomi.entity.SeckillProduct;
import com.mall.xiaomi.entity.SeckillTime;
import com.mall.xiaomi.service.Imp.SeckillProductServiceImp;
import com.mall.xiaomi.service.SeckillProductService;
import com.mall.xiaomi.util.Result;
import com.mall.xiaomi.vo.SeckillProductVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: wdd
 * @Date: 2020-03-28 19:58
 * @Description:
 */
@RestController
@RequestMapping("/seckill/product")
public class SeckillProductController {


    @Autowired
    private SeckillProductService seckillProductService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据时间id获取对应时间的秒杀商品列表
     * @param timeId
     * @return
     */
    @GetMapping("/time/{timeId}")
    public Result getProduct(@PathVariable String timeId) {
        List<SeckillProductVo> seckillProductVos = seckillProductService.getProduct(timeId);
        return Result.success(seckillProductVos);
    }

    /**
     * 获取秒杀商品
     * @param seckillId
     * @return
     */
    @GetMapping("/{seckillId}")
    public Result getSeckill(@PathVariable String seckillId) {
        SeckillProductVo seckillProductVo = seckillProductService.getSeckill(seckillId);
        return Result.success(seckillProductVo);
    }

    /**
     * 获取时间段
     * @return
     */
    @GetMapping("/time")
    public Result getTime() {
        List<SeckillTime> seckillTimes = seckillProductService.getTime();
        return Result.success(seckillTimes);
    }

    /**
     * 添加秒杀商品
     * @param seckillProduct
     * @return
     */
    @PostMapping("")
    public Result addSeckillProduct(@RequestBody SeckillProduct seckillProduct) {
        seckillProductService.addSeckillProduct(seckillProduct);

        return Result.success("添加成功");
    }

    /**
     * 开始秒杀
     * @param seckillId
     * @return
     */
    @PostMapping("/seckill/{seckillId}")
    public Result seckillProduct(@PathVariable String seckillId, @CookieValue("XM_TOKEN") String cookie) {
        // 先判断cookie是否存在，和redis校验
        Integer userId = (Integer) redisTemplate.opsForHash().get(cookie, "userId");
        seckillProductService.seckillProduct(seckillId, userId);
        return Result.success("排队中");
    }


}
