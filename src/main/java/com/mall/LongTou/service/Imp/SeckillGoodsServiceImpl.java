package com.mall.LongTou.service.Imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.LongTou.common.BusinessException;
import com.mall.LongTou.common.ExceptionEnum;
import com.mall.LongTou.common.UserHolder;
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

import javax.validation.constraints.NotNull;
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

    @Autowired
    private UserHolder userHolder;

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
    public String createSeckillOrder(@NotNull Integer userId, Integer seckillGoodsId, Integer quantity) {
        //参数校验？校验过了吧
        //查开始秒杀活动开始时间
        SeckillGoods seckillGoods = query().eq("seckill_goods_id", seckillGoodsId).one();

        //这个方法报错  不知道为啥
        //   SeckillGoods seckillGoods = seckillGoodsMapper.selectById(seckillGoodsId);
        if (seckillGoods == null) {
            throw new BusinessException(ExceptionEnum.SECKILL_GOODS_NOT_EXIST);
        }

        SeckillActivity seckillActivity = seckillActivityMapper.selectById(seckillGoods.getActivityId());
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(seckillActivity.getStartTime())) {
            throw new BusinessException(ExceptionEnum.SECKILL_ACTIVITY_NOT_START);
        }

        if (now.isAfter(seckillActivity.getEndTime())) {
            throw new BusinessException(ExceptionEnum.SECKILL_ALREADY_ENDED);

        }

        //活动开始  检验库存
        Integer stock = seckillGoods.getSeckillStock();
        //获取版本号
        Integer version = seckillGoods.getVersion();
        //库存不足
        if (stock <= 0) {
            throw new BusinessException(ExceptionEnum.SECKILL_STOCK_INSUFFICIENT);


        }
        //查询用户是否下单 一人一单子
        Orders order = ordersMapper.selectOne(Wrappers.<Orders>lambdaQuery()
                .eq(Orders::getUserId, userId)
                .eq(Orders::getSeckillGoodsId, seckillGoods.getSeckillGoodsId()));

        if(order != null){
            throw new BusinessException(ExceptionEnum.USER_REPEAT_SECOND_KILL);
        }
        try {

            //乐观锁版本号删减库存
            boolean updated = update().eq("seckill_goods_id", seckillGoodsId)
                    .eq("version", version)
                    .setSql("seckill_stock = seckill_stock -1 ")
                    .setSql("version = version + 1 ")
                    .gt("seckill_stock", 0).update();

            if (!updated) {
                // 可能是库存不足或版本号冲突
                throw new BusinessException(ExceptionEnum.SECKILL_STOCK_INSUFFICIENT);
            }
            //生成订单
            Product product = productMapper.selectById(seckillGoods.getProductId());
            Orders orders = new Orders();
            //设置订单相关id
            orders.setOrderId(generateOrderId(userId));
            orders.setSeckillGoodsId(seckillGoodsId);
            orders.setProductId(product.getProductId());
            orders.setUserId(userId);


            orders.setOrderTime(LocalDateTime.now());
            //未支付
            orders.setOrderStatus(0);

            orders.setProductNameSnapshot(product.getProductName());
            orders.setSeckillPriceSnapshot(product.getProductPrice());
            orders.setProductPrice(product.getProductPrice());
            orders.setProductNum(1);
            ordersMapper.insert(orders);
            return orders.getOrderId();
        } catch (Exception e) {

            e.printStackTrace();

             throw new BusinessException(ExceptionEnum.ADD_ORDER_ERROR);
        }


    }

    private String generateOrderId(Integer userId) {
        // 简单实现：时间戳 + 用户ID + 随机数，实际应用应替换为分布式ID生成器
        return System.currentTimeMillis() + "" + userId + (int)(Math.random() * 10000);
    }

}