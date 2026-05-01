package com.mall.LongTou.controller;

import com.mall.LongTou.common.Result;
import com.mall.LongTou.dto.SeckillOrderDTO;
import com.mall.LongTou.service.SeckillActivityService;
import com.mall.LongTou.service.SeckillGoodsService;
import com.mall.LongTou.vo.SeckillActivityVO;
import com.mall.LongTou.vo.SeckillGoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/seckill")
@Validated
public class SeckillBusinessController {


    @Autowired
    private SeckillGoodsService seckillGoodsService;

    @Autowired
    private SeckillActivityService seckillActivityService;

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

    //重点  TODO
    @PostMapping("/order")
    public Result<String> createSeckillOrder(@Valid @RequestBody SeckillOrderDTO dto) {
        //调用秒杀商品服务
        String ordersId = seckillGoodsService.createSeckillOrder(dto.getUserId(), dto.getSeckillGoodsId(), dto.getQuantity());

        return Result.success(ordersId);
    }
}