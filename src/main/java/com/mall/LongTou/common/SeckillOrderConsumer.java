package com.mall.LongTou.common;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.mall.LongTou.config.RabbitMQConfig;
import com.mall.LongTou.entity.Orders;
import com.mall.LongTou.entity.Product;
import com.mall.LongTou.entity.SeckillActivity;
import com.mall.LongTou.entity.SeckillGoods;
import com.mall.LongTou.mapper.OrdersMapper;
import com.mall.LongTou.mapper.ProductMapper;
import com.mall.LongTou.mapper.SeckillActivityMapper;
import com.mall.LongTou.mapper.SeckillGoodsMapper;
import com.mall.LongTou.service.SeckillGoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

//先通过消息队列晓峰预减库存 之后在消费者里查数据库

//spring自动创建
@Component
@Slf4j
public class SeckillOrderConsumer {

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private SeckillActivityMapper seckillActivityMapper;

    @Autowired
    private ProductMapper  productMapper;

    @Autowired
    private OrdersMapper ordersMapper;


    @RabbitListener(queues = RabbitMQConfig.SECKILL_QUEUE)
    @Transactional(rollbackFor = Exception.class)//开启事务  防止回滚少买
    public void HandleSeckillOrderQueue(SeckillOrderMessage message){

        //从消息中获取订单所需信息
        Integer seckillGoodsId = message.getSeckillGoodsId();
        Integer quantity = message.getQuantity();
        Integer userId = message.getUserId();

        // 1. 幂等性检查：是否已经下单成功
        LambdaQueryWrapper<Orders> existWrapper = new LambdaQueryWrapper<Orders>()
                .eq(Orders::getUserId, userId)
                .eq(Orders::getSeckillGoodsId, seckillGoodsId);
        if (ordersMapper.selectCount(existWrapper) > 0) {
            log.info("重复消费或重复下单，已忽略。userId={}, seckillGoodsId={}", userId, seckillGoodsId);
            return;   // 直接返回，不扣库存，不抛异常，消息 ack
        }

        SeckillGoods seckillGoods = seckillGoodsMapper.selectOne(new LambdaQueryWrapper<SeckillGoods>()
                .eq(SeckillGoods::getSeckillGoodsId, seckillGoodsId));

        SeckillActivity activity = seckillActivityMapper.selectById(seckillGoods.getActivityId());
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(activity.getStartTime()) || now.isAfter(activity.getEndTime())) {
            log.warn("活动未开始或已结束, seckillGoodsId={}", seckillGoodsId);
            return;
        }

        //再查一次库存

        if (seckillGoods == null || seckillGoods.getSeckillStock() <= 0) {
            log.warn("库存已耗尽或商品不存在, seckillGoodsId={}", seckillGoodsId);
            return; // 可以直接返回，或者记录失败日志
        }

        //创建订单
        //乐观锁版本号删减库存
        LambdaUpdateWrapper<SeckillGoods> wrapper = new LambdaUpdateWrapper<SeckillGoods>()
                .eq(SeckillGoods::getSeckillGoodsId, seckillGoodsId)
                .eq(SeckillGoods::getVersion, seckillGoods.getVersion())
                .gt(SeckillGoods::getSeckillStock, 0)
                .setSql("seckill_stock = seckill_stock -" + quantity)
                .setSql("version = version + 1");

        int updated = seckillGoodsMapper.update(null, wrapper);
        if (updated == 0) {
            log.warn("乐观锁扣减库存失败，可能库存不足或版本冲突, seckillGoodsId={}", seckillGoodsId);
            return;
        }

        // 4. 生成订单（唯一索引防重复）
        Orders order = new Orders();
        order.setOrderId(generateOrderId(userId));
        order.setSeckillGoodsId(seckillGoodsId);
        order.setUserId(userId);
        order.setProductId(seckillGoods.getProductId());
        order.setOrderTime(LocalDateTime.now());
        order.setOrderStatus(0); // 未支付
        // 冗余商品信息...
        Product product = productMapper.selectById(seckillGoods.getProductId());
        order.setProductNameSnapshot(product.getProductName());
        order.setSeckillPriceSnapshot(seckillGoods.getSeckillPrice());

        order.setProductPrice(product.getProductPrice());
        order.setProductNum(quantity);

        try {
            ordersMapper.insert(order);
            return;
        } catch (DuplicateKeyException e) {
            log.warn("用户重复下单, userId={}, seckillGoodsId={}", userId, seckillGoodsId);
            // 如果重复，要回滚刚才扣减的库存？这里简单做法：因为唯一索引阻止了插入，需要回滚事务
            // 但事务会自动回滚，库存已经扣减了，会造成少卖。改进：先检查订单是否存在再扣库存。
            // 实际生产中建议把 唯一性检查放在扣库存之前（比如 Redis 里已经标记过了，这里只是兜底）。
            throw new RuntimeException("重复下单");
        }

    }



    //简单的订单id生成器  也可以改成雪花算法
    // 加一个IdWorker 但其实依旧是调用一个函数的事
    private String generateOrderId(Integer userId) {
        // 简单实现：时间戳 + 用户ID + 随机数，实际应用应替换为分布式ID生成器
        return System.currentTimeMillis() + "" + userId + (int)(Math.random() * 10000);
    }
}
