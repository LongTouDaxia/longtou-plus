package com.mall.xiaomi.service.Imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.xiaomi.exception.ExceptionEnum;
import com.mall.xiaomi.exception.XmException;
import com.mall.xiaomi.mapper.OrderMapper;
import com.mall.xiaomi.mapper.ProductMapper;
import com.mall.xiaomi.mapper.SeckillProductMapper;
import com.mall.xiaomi.mapper.ShoppingCartMapper;
import com.mall.xiaomi.entity.Order;
import com.mall.xiaomi.entity.Product;
import com.mall.xiaomi.entity.SeckillProduct;
import com.mall.xiaomi.entity.ShoppingCart;
import com.mall.xiaomi.service.OrderService;
import com.mall.xiaomi.util.IdWorker;
import com.mall.xiaomi.vo.CartVo;
import com.mall.xiaomi.vo.OrderVo;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther: wdd
 * @Date: 2020-03-19 13:21
 * @Description:
 */
@Service
public class OrderServiceImp extends ServiceImpl<OrderMapper,Order> implements OrderService {

        @Autowired
        private IdWorker idWorker;
        @Autowired
        private RedisTemplate redisTemplate;
        @Autowired
        private OrderMapper orderMapper;
        @Autowired
        private ShoppingCartMapper cartMapper;
        @Autowired
        private ProductMapper productMapper;
        @Autowired
        private SeckillProductMapper seckillProductMapper;

        private final static String SECKILL_PRODUCT_USER_LIST = "seckill:product:user:list";

       @Transactional
        public void addOrder(List<CartVo> cartVoList, Integer userId) {
                // 先添加订单
                String orderId = idWorker.nextId() + ""; // 订单id
                long time = new Date().getTime(); // 订单生成时间
                for (CartVo cartVo : cartVoList) {
                        Integer productId = cartVo.getProductId();
                        Integer saleNum = cartVo.getNum();
                        Order order = new Order();
                        order.setOrderId(orderId);
                        order.setOrderTime(time);
                        order.setProductNum(saleNum);
                        order.setProductId(productId);
                        order.setProductPrice(cartVo.getPrice());
                        order.setUserId(userId);
                        try {
                                orderMapper.insert(order);
                        } catch (Exception e) {
                                e.printStackTrace();
                                throw new XmException(ExceptionEnum.ADD_ORDER_ERROR);
                        }
                        // 减去商品库存,记录卖出商品数量
                        // TODO : 此处会产生多线程问题，即不同用户同时对这个商品操作，此时会导致数量不一致问题,


                        //所以利用数据库原子性+乐观锁+重试机制 通过产品数量>0 ,版本号是否相等 来进行判断, 保证库存不会出现负数
                        // 获取当前商品信息
                        Product product = productMapper.selectById(productId);
                        //Product product = productMapper.selectByPrimaryKey(productId);
                        if (product == null) {
                                throw new RuntimeException("商品不存在");
                        }

                        int currentVersion = product.getVersion();

                        // 尝试更新库存和版本号
                        int retryTimes = 3;
                        boolean success = false;

                        while (retryTimes > 0 && !success) {
                                int updateCount = productMapper.update(product, null);
                             //   int updateCount = productMapper.updateStockByIdAndVersion(productId, saleNum, currentVersion);
                                if (updateCount > 0) {
                                        success = true;
                                } else {
                                        retryTimes--;
                                        // 重新获取最新的 product 信息
                                        product = productMapper.selectById(productId);

                                        //product = productMapper.selectByPrimaryKey(productId);
                                        currentVersion = product.getVersion();
                                }
                        }
                }
                // 删除购物车
                ShoppingCart cart = new ShoppingCart();
                cart.setUserId(userId);
                try {
                        int flag = cartMapper.deleteById(cart.getId());
                        //int count = cartMapper.delete(cart);
                        if (flag == 0) {
                                throw new XmException(ExceptionEnum.ADD_ORDER_ERROR);
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                        throw new XmException(ExceptionEnum.ADD_ORDER_ERROR);
                }

        }

        public List<List<OrderVo>> getOrder(Integer userId) {
                List<OrderVo> list = null;
                ArrayList<List<OrderVo>> ret = new ArrayList<>();
                try {
                        list = orderMapper.getOrderVoByUserId(userId);
                        if (ArrayUtils.isEmpty(list.toArray())) {
                                throw new XmException(ExceptionEnum.GET_ORDER_NOT_FOUND);
                        }
                        // 将同一个订单放在一组
                        Map<String, List<OrderVo>> collect = list.stream().collect(Collectors.groupingBy(Order::getOrderId));

                        // 将分组结果按 order_time 降序排列
                        List<List<OrderVo>> sortedCollect = collect.values().stream()
                                // 将每个分组内的数据按照 order_time 降序排列
                                .sorted((list1, list2) -> {
                                        OrderVo order1 = list1.stream().max(Comparator.comparing(OrderVo::getOrderTime)).orElse(null);
                                        OrderVo order2 = list2.stream().max(Comparator.comparing(OrderVo::getOrderTime)).orElse(null);
                                        if (order1 == null && order2 == null) return 0;
                                        if (order1 == null) return 1;
                                        if (order2 == null) return -1;
                                        return order2.getOrderTime().compareTo(order1.getOrderTime());
                                })
                                .collect(Collectors.toList());

                        ret.addAll(sortedCollect);
                } catch (XmException e) {
                        e.printStackTrace();
                        throw new XmException(ExceptionEnum.GET_ORDER_ERROR);
                }
                return ret;
        }

        @Transactional
        public void addSeckillOrder(String seckillId, String userId) {

                Long count = query()
                        .eq("seckillId", seckillId)
                        .eq("userId", userId)
                        .count();

                if(count>0){
                        //已经购买过一次
                        throw new XmException(ExceptionEnum.GET_SECKILL_IS_REUSE);
                }
                // 订单id
                String orderId = idWorker.nextId() + "";
                // 商品id
                SeckillProduct seckillProduct = new SeckillProduct();
                seckillProduct.setSeckillId(Integer.parseInt(seckillId));
                SeckillProduct one = seckillProductMapper.selectById(seckillProduct.getSeckillId());
        //        SeckillProduct one = seckillProductMapper.selectOne(seckillProduct);
                Integer productId = one.getProductId();
                // 秒杀价格
                Double price = one.getSeckillPrice();

                // 订单封装
                Order order = new Order();
                order.setOrderId(orderId);
                order.setProductId(productId);
                order.setProductNum(1);
                order.setUserId(Integer.parseInt(userId));
                order.setOrderTime(new Date().getTime());
                order.setProductPrice(price);

                try {
                        save(order);
                       // orderMapper.insert(order);
                        // 减库存
                        update()
                                .setSql("seckill_stock = seckill_stock -1")
                                        .eq("seckill_id", seckillId)
                                                .gt("seckill_stock",0)
                                                        .update();
                      //  seckillProductMapper.decrStock(one.getSeckillId());
                } catch (Exception e) {
                        e.printStackTrace();
                        throw new XmException(ExceptionEnum.ADD_ORDER_ERROR);
                }

                // 订单创建成功, 将用户写入redis, 防止多次抢购
               // redisTemplate.opsForList().leftPush(SECKILL_PRODUCT_USER_LIST + seckillId, userId);

        }
}
