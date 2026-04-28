package com.mall.xiaomi.controller;

import com.mall.xiaomi.service.Imp.OrderServiceImp;
import com.mall.xiaomi.service.OrderService;
import com.mall.xiaomi.util.Result;
import com.mall.xiaomi.vo.CartVo;
import com.mall.xiaomi.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: wdd
 * @Date: 2020-03-19 13:25
 * @Description:
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private OrderService orderService;

    @PostMapping("")
    public Result addOrder(@RequestBody List<CartVo> cartVoList, @CookieValue("XM_TOKEN") String cookie) {
        // 先判断cookie是否存在，和redis校验
        Integer userId = (Integer) redisTemplate.opsForHash().get(cookie, "userId");
        orderService.addOrder(cartVoList, userId);
        return Result.success("下单成功");
    }

    @GetMapping("")
    public Result getOrder(@CookieValue("XM_TOKEN") String cookie) {
        // 先判断cookie是否存在，和redis校验
        Integer userId = (Integer) redisTemplate.opsForHash().get(cookie, "userId");
        List<List<OrderVo>> orders = orderService.getOrder(userId);
        return Result.success(orders);
    }

}
