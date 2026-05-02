package com.mall.LongTou.common;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall.LongTou.entity.Orders;
import com.mall.LongTou.mapper.OrdersMapper;
import com.mall.LongTou.mapper.SeckillGoodsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.mall.LongTou.util.SeckillKey.*;

@Slf4j
@Component
public class CancelOrderConsumer {


    private final OrdersMapper ordersMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final SeckillGoodsMapper seckillGoodsMapper;

    public CancelOrderConsumer(OrdersMapper ordersMapper, StringRedisTemplate stringRedisTemplate, SeckillGoodsMapper seckillGoodsMapper) {
        this.ordersMapper = ordersMapper;
        this.stringRedisTemplate = stringRedisTemplate;
        this.seckillGoodsMapper = seckillGoodsMapper;
    }

    @RabbitListener(queues = CANCEL_QUEUE)
    @Transactional(rollbackFor = BusinessException.class)
    //只有runtimeexception才会事务回滚
    public void HandleOrderCancel(String orderId){

        log.info("收到订单取消的延迟消息，orderId：{}",orderId);

        Orders orders = ordersMapper.selectOne(new LambdaQueryWrapper<Orders>()
                .eq(Orders::getOrderId, orderId));

        //这种多id的数据库表不能直接用selecyById，会报错  还是用wrapper吧
       // Orders orders = ordersMapper.selectById(orderId);
        //幂等性检查，订单是否已支付或者取消
        if(orders==null  || orders.getOrderStatus() != 0){
            log.info("订单状态已变更，无需处理，orderId：{}",orderId);
            return ;
        }

        try {
            //设置订单状态为已取消
            orders.setOrderStatus(2);
            //设置取消时间 我这里数据库设计的没有很完善  没有这个字段 先不管

            //更新数据库
            ordersMapper.update(orders, new LambdaQueryWrapper<Orders>()
                    .eq(Orders::getOrderId, orderId));

            //先数据库再redis  防止数据库异常回滚而redis无法回滚
            //回滚数据库库存
            seckillGoodsMapper.incrstock(orders.getSeckillGoodsId(), orders.getProductNum());

            log.info("数据库库存回滚成功，orderId:{}", orderId);
            //回滚商品库存  库存和用户信息
            String stockKey = SECKILL_STOCK_KEY + orders.getSeckillGoodsId();
            String userKey = SECKILL_USER_KEY + orders.getSeckillGoodsId();

            stringRedisTemplate.opsForValue().increment(stockKey, orders.getProductNum());
            //Lua脚本里这个key存的是集合set类型
            stringRedisTemplate.opsForSet().remove(userKey, String.valueOf(orders.getUserId()));

            log.info("redis数据回滚成功，orderId:{}",orderId);


        }catch (RuntimeException e){
            throw new BusinessException(ExceptionEnum.ORDER_ROOLBACK_ERROR);
        }
    }
}
