package com.mall.LongTou.controller;

import com.mall.LongTou.common.Result;
import com.mall.LongTou.dto.OrderCreateDTO;
import com.mall.LongTou.entity.Orders;
import com.mall.LongTou.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/order")
@Validated
public class OrdersController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public Result<Void> createOrder(@Valid @RequestBody OrderCreateDTO dto) {
        orderService.createOrder(dto);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<List<Orders>> getUserOrders(@RequestParam @NotNull(message = "用户ID不能为空") Integer userId) {
        List<Orders> list = orderService.getUserOrders(userId);
        return Result.success(list);
    }

    @PutMapping("/cancel/{orderId}")
    public Result<Void> cancelOrder(@PathVariable @NotBlank(message = "订单号不能为空") String orderId,
                                    @RequestParam @NotNull(message = "用户ID不能为空") Integer userId) {
        orderService.cancelOrder(orderId, userId);
        return Result.success();
    }
}