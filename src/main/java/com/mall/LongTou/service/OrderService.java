package com.mall.LongTou.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.LongTou.dto.OrderCreateDTO;
import com.mall.LongTou.entity.Orders;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public interface OrderService extends IService<Orders> {

    void createOrder(@Valid OrderCreateDTO dto);

    List<Orders> getUserOrders();

    void cancelOrder(@NotBlank(message = "订单号不能为空") String orderId, @NotNull(message = "用户ID不能为空") Integer userId);
}
