package com.mall.LongTou.service.Imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.LongTou.common.BusinessException;
import com.mall.LongTou.common.ExceptionEnum;
import com.mall.LongTou.common.UserHolder;
import com.mall.LongTou.dto.OrderCreateDTO;
import com.mall.LongTou.dto.UserDTO;
import com.mall.LongTou.entity.*;
import com.mall.LongTou.mapper.OrdersMapper;
import com.mall.LongTou.mapper.ProductMapper;
import com.mall.LongTou.mapper.ShoppingCartMapper;
import com.mall.LongTou.service.OrderService;
import com.mall.LongTou.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Auther: wdd
 * @Date: 2020-03-19 13:21
 * @Description:
 */
@Service
public class OrderServiceImp extends ServiceImpl<OrdersMapper, Orders> implements OrderService {

        @Autowired
        private UserHolder userHolder;
        @Autowired
        private RedisTemplate redisTemplate;
        @Autowired
        private OrdersMapper ordersMapper;

        @Autowired
        private ShoppingCartMapper shoppingCartMapper;
        @Autowired
        private ProductMapper productMapper;

        private final static String SECKILL_PRODUCT_USER_LIST = "seckill:product:user:list";

    @Override
    public void createOrder(OrderCreateDTO dto) {
        //先查库存


    }

    @Override
    public List<Orders> getUserOrders() {

        UserDTO user = userHolder.getUser();
        String userId = user.getUserId();
        if (userId == null) {
            throw new BusinessException(ExceptionEnum.USER_CANTOT_NULL);
        }
        List<Orders> userOrder = query().eq("user_id", userId).list();
        //为空也直接返回  前端自己校验
        return userOrder;
    }

    @Override
    public void cancelOrder(String orderId, Integer userId) {
        if (orderId == null || userId == null) {
            throw new BusinessException(ExceptionEnum.ORDER_NOT_FOUND);
        }
        // 查询订单
        Orders order = ordersMapper.selectById(orderId);
        if (order == null) {
            throw new BusinessException(ExceptionEnum.ORDER_NOT_FOUND);
        }
        // 校验归属
        if (!order.getUserId().equals(userId)) {
            throw new BusinessException(ExceptionEnum.ORDER_NOT_FOUND, "订单不属于该用户");
        }
        // 只有待支付状态可取消
        if (order.getOrderStatus() != 0) {
            throw new BusinessException(ExceptionEnum.ORDER_CANNOT_CANCEL);
        }
        // 更新状态为已取消
        order.setOrderStatus(2);
        int rows = ordersMapper.updateById(order);
        if (rows != 1) {
            throw new BusinessException(ExceptionEnum.ORDER_CREATE_EXCEPTION, "取消失败");
        }

        // 注意：取消订单需要归还库存。但这里简单处理，企业级一般会加回。
        // 此处需要根据业务决定是否归还。如果归还，需查询商品并增加库存。
        // 为了避免超卖，归还时也需考虑并发。
       // restoreProductStock(order.getProductId(), order.getProductNum());

    }
}
