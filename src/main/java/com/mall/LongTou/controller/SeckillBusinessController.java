package com.mall.LongTou.controller;

import com.mall.LongTou.common.ExceptionEnum;
import com.mall.LongTou.common.Result;
import com.mall.LongTou.dto.SeckillOrderDTO;
import com.mall.LongTou.service.SeckillActivityService;
import com.mall.LongTou.service.SeckillGoodsService;
import com.mall.LongTou.vo.SeckillActivityVO;
import com.mall.LongTou.vo.SeckillGoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

import static com.mall.LongTou.util.SeckillKey.SECKILL_TOKEN_KEY;

@RestController
@RequestMapping("/seckill")
@Validated
public class SeckillBusinessController {


    @Autowired
    private SeckillGoodsService seckillGoodsService;

    @Autowired
    private SeckillActivityService seckillActivityService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/activities")
    public Result<List<SeckillActivityVO>> getActiveActivities() {
        List<SeckillActivityVO> list = seckillActivityService.getCurrentActivities();
        return Result.success(list);
    }

    @GetMapping("/activities/{activityId}/goods")
    public Result<List<SeckillGoodsVO>> getActivityGoods(@PathVariable @NotNull(message = "活动ID不能为空") Integer activityId) {
        List<SeckillGoodsVO> list = seckillGoodsService.getByActivityId(activityId);
        return Result.success(list);
    }

    //重点
    @PostMapping("/order")
    public Result<Map<String, String>> createSeckillOrder(@Valid @RequestBody SeckillOrderDTO dto) {
        //调用秒杀商品服务
        //返回订单tokenId和url路径
        Map<String,String> orderData = seckillGoodsService.createSeckillOrder(dto.getUserId(), dto.getSeckillGoodsId(), dto.getQuantity());

        return Result.success(orderData);
    }



}