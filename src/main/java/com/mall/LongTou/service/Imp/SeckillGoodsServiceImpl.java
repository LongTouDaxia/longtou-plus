package com.mall.LongTou.service.Imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.LongTou.common.BusinessException;
import com.mall.LongTou.common.ExceptionEnum;
import com.mall.LongTou.entity.Orders;
import com.mall.LongTou.entity.Product;
import com.mall.LongTou.entity.SeckillActivity;
import com.mall.LongTou.entity.SeckillGoods;
import com.mall.LongTou.mapper.OrdersMapper;
import com.mall.LongTou.mapper.ProductMapper;
import com.mall.LongTou.mapper.SeckillActivityMapper;
import com.mall.LongTou.mapper.SeckillGoodsMapper;
import com.mall.LongTou.service.SeckillGoodsService;
import com.mall.LongTou.vo.SeckillGoodsVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeckillGoodsServiceImpl extends ServiceImpl<SeckillGoodsMapper, SeckillGoods> implements SeckillGoodsService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private SeckillActivityMapper seckillActivityMapper;

    @Autowired
    private SeckillGoodsMapper seckillGoodsMapper;


    @Override
    public boolean decreaseStock(Integer seckillGoodsId, Integer quantity, Integer version) {
        return false;
    }

    @Override
    public List<SeckillGoodsVO> getByActivityId(Integer activityId) {
        if (activityId == null) {
            throw new BusinessException(ExceptionEnum.SECKILL_ACTIVITY_NOT_EXIST);
        }
        // 查询活动是否存在且有效（可选）
        SeckillActivity activity = seckillActivityMapper.selectById(activityId);
        if (activity == null || activity.getStatus() != 1) {
            throw new BusinessException(ExceptionEnum.SECKILL_ACTIVITY_NOT_EXIST);
        }

        LambdaQueryWrapper<SeckillGoods> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SeckillGoods::getActivityId, activityId);
        List<SeckillGoods> goodsList = seckillGoodsMapper.selectList(wrapper);
        if (goodsList.isEmpty()) {
            throw new BusinessException(ExceptionEnum.SECKILL_PRODUCT_NULL);
        }

        // 批量查询商品信息
        List<Integer> productIds = goodsList.stream().map(SeckillGoods::getProductId).collect(Collectors.toList());
        List<Product> products = productMapper.selectBatchIds(productIds);
        java.util.Map<Integer, Product> productMap = products.stream().collect(Collectors.toMap(Product::getProductId, p -> p));

        return goodsList.stream().map(goods -> {
            SeckillGoodsVO vo = new SeckillGoodsVO();
            BeanUtils.copyProperties(goods, vo);
            Product product = productMap.get(goods.getProductId());
            if (product != null) {
                vo.setProductName(product.getProductName());
                vo.setProductPicture(product.getProductPicture());
            }

            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createSeckillOrder(Integer userId, Integer seckillGoodsId, Integer quantity) {
        // 1. 参数校验
        if (userId == null || seckillGoodsId == null || quantity == null || quantity <= 0) {
            throw new BusinessException(ExceptionEnum.PARAM_ERROR, "参数不合法");
        }

        // 2. 查询秒杀商品（加行锁，悲观锁方式，可选；这里使用乐观锁，先查再更新）
        SeckillGoods seckillGoods = seckillGoodsMapper.selectById(seckillGoodsId);
        if (seckillGoods == null) {
            throw new BusinessException(ExceptionEnum.SECKILL_GOODS_NOT_EXIST);
        }

        // 3. 校验活动时间
        SeckillActivity activity = seckillActivityMapper.selectById(seckillGoods.getActivityId());
        if (activity == null || activity.getStatus() != 1) {
            throw new BusinessException(ExceptionEnum.SECKILL_ACTIVITY_NOT_EXIST);
        }

        //判断活动是否开始
        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(activity.getStartTime())) {
            throw new BusinessException(ExceptionEnum.SECKILL_NOT_START);
        }
        if (now.isAfter( activity.getEndTime())) {
            throw new BusinessException(ExceptionEnum.SECKILL_ALREADY_ENDED);
        }

        // 4. 限购校验
        if (quantity > seckillGoods.getLimitPerUser()) {
            throw new BusinessException(ExceptionEnum.USER_HAS_SECOND_KILL_LIMIT);
        }

        // 5. 防止重复下单: 检查是否已有成功订单（同一用户同一秒杀商品）
        LambdaQueryWrapper<Orders> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.eq(Orders::getUserId, userId)
                .eq(Orders::getSeckillGoodsId, seckillGoodsId)
                .ne(Orders::getOrderStatus, 2); // 排除已取消的订单（可根据需求，已取消的可以再买？一般不允许）
        Long existed = ordersMapper.selectCount(orderWrapper);
        if (existed > 0) {
            throw new BusinessException(ExceptionEnum.USER_REPEAT_SECOND_KILL);
        }

        // 6. 乐观锁扣减库存
        int version = seckillGoods.getVersion();
         /*       int updated = seckillGoodsMapper.decreaseStock(seckillGoodsId, quantity, version);//mysql实现
                if (updated == 0) {
                        throw new BusinessException(ExceptionEnum.SECKILL_STOCK_INSUFFICIENT);
                }

          */

        // 7. 获取商品信息（快照）
        Product product = productMapper.selectById(seckillGoods.getProductId());
        if (product == null) {
            throw new BusinessException(ExceptionEnum.PRODUCT_NOT_FOUND);
        }

        // 8. 生成订单号（简单示例，实际使用雪花算法）
        String orderId = generateOrderId(userId);

        // 9. 创建订单实体
        Orders order = new Orders();
        order.setOrderId(orderId);
        order.setUserId(userId);
        order.setProductId(product.getProductId());
        order.setProductNum(quantity);
        order.setProductPrice(product.getProductPrice());          // 原价快照
        order.setOrderTime(now);
        order.setOrderStatus(0);                                   // 待支付
        order.setSeckillGoodsId(seckillGoodsId);
        order.setSeckillPriceSnapshot(seckillGoods.getSeckillPrice().doubleValue());
        order.setProductNameSnapshot(product.getProductName());

        int insert = ordersMapper.insert(order);
        if (insert != 1) {
            throw new BusinessException(ExceptionEnum.ADD_ORDER_ERROR);
        }
    }

    private String generateOrderId(Integer userId) {
        // 简单实现：时间戳 + 用户ID + 随机数，实际应用应替换为分布式ID生成器
        return System.currentTimeMillis() + "" + userId + (int)(Math.random() * 10000);
    }

}