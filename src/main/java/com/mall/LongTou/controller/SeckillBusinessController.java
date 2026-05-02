package com.mall.LongTou.controller;

import com.mall.LongTou.common.ExceptionEnum;
import com.mall.LongTou.common.Result;
import com.mall.LongTou.dto.SeckillOrderDTO;
import com.mall.LongTou.service.SeckillActivityService;
import com.mall.LongTou.service.SeckillGoodsService;
import com.mall.LongTou.vo.SeckillActivityVO;
import com.mall.LongTou.vo.SeckillGoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

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
    public Result<String> createSeckillOrder(@Valid @RequestBody SeckillOrderDTO dto) {
        //调用秒杀商品服务
        String ordersId = seckillGoodsService.createSeckillOrder(dto.getUserId(), dto.getSeckillGoodsId(), dto.getQuantity());

        return Result.success(ordersId);
    }

    //前端轮询订单结果
    @GetMapping("/result/{orderToken}")
    public Result<String>  getOrderResult(@PathVariable String orderToken){

        Object val = stringRedisTemplate.opsForValue().get(SECKILL_TOKEN_KEY + orderToken);
        if (val == null) {
            return Result.error(ExceptionEnum.GET_RESULT_ERROR);
        }
        String result = val.toString();
        if (result.startsWith("success:")) {
            String orderId = result.substring(8);
            //下单成功，返回订单id
            return Result.success(orderId);
        } else if (result.startsWith("FAILED:")) {
            return Result.error(result.substring(7));
        } else if ("processing".equals(result)) {
            return Result.error(ExceptionEnum.RESULT_PROCESSING);
        }
        return Result.error(ExceptionEnum.DONT_KNOW_ERROR);


        }
}