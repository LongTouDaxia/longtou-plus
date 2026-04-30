package com.mall.xiaomi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.xiaomi.entity.Order;
import com.mall.xiaomi.util.Result;
import com.mall.xiaomi.vo.CartVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService extends IService<Order> {
    Result addOrder(List<CartVo> cartVoList);

    Result getOrder();
}
