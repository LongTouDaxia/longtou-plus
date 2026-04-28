package com.mall.xiaomi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.xiaomi.entity.Order;
import com.mall.xiaomi.vo.CartVo;
import com.mall.xiaomi.vo.OrderVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService extends IService<Order> {
    void addOrder(List<CartVo> cartVoList, Integer userId);

    List<List<OrderVo>> getOrder(Integer userId);
}
