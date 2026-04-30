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
    public Result addOrder(@RequestBody List<CartVo> cartVoList) {


        return orderService.addOrder(cartVoList);
    }

    @GetMapping("")
    public Result getOrder() {

        return orderService.getOrder();
    }

}
