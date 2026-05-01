package com.mall.LongTou.service.Imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.LongTou.common.BusinessException;
import com.mall.LongTou.common.ExceptionEnum;
import com.mall.LongTou.common.SeckillOrderMessage;
import com.mall.LongTou.common.UserHolder;
import com.mall.LongTou.entity.Product;
import com.mall.LongTou.entity.SeckillActivity;
import com.mall.LongTou.entity.SeckillGoods;
import com.mall.LongTou.mapper.OrdersMapper;
import com.mall.LongTou.mapper.ProductMapper;
import com.mall.LongTou.mapper.SeckillActivityMapper;
import com.mall.LongTou.mapper.SeckillGoodsMapper;
import com.mall.LongTou.service.SeckillGoodsService;
import com.mall.LongTou.vo.SeckillGoodsVO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
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
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;


    private DefaultRedisScript<Long> stockLuaScript;
    @PostConstruct
    public void init() {
        DefaultRedisScript<Long> stockLuaScript = new DefaultRedisScript<>();

        stockLuaScript.setScriptText(
                "local stock_key = KEYS[1]\n" +
                        "local user_key = KEYS[2]\n" +
                        "local user_id = ARGV[1]\n" +
                        "local stock = redis.call('get', stock_key)\n" +
                        "if tonumber(stock) <= 0 then return 0 end\n" +
                        "local bought = redis.call('sismember', user_key, user_id)\n" +
                        "if bought == 1 then return 1 end\n" +
                        "redis.call('decr', stock_key)\n" +
                        "redis.call('sadd', user_key, user_id)\n" +
                        "return 2\n"
        );
        stockLuaScript.setResultType(Long.class);
    }

    //创建秒杀订单
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createSeckillOrder(@NotNull Integer userId, Integer seckillGoodsId, Integer quantity) {
        // 1. 参数校验
        if (userId == null || seckillGoodsId == null || quantity == null || quantity <= 0) {
            throw new BusinessException(ExceptionEnum.PARAM_ERROR);
        }
        String stockKey = "seckill:stock:" + seckillGoodsId;
        String userKey = "seckill:user:" + seckillGoodsId;

        //获取用户状态  0表示库存不足 1表示用户已购买 2表示下单成功
        //redis之星lua教本
        Long result = redisTemplate.execute(stockLuaScript,
                Arrays.asList(stockKey, userKey),
                userId.toString());
        if (result == 0) {
            throw new BusinessException(ExceptionEnum.SECKILL_STOCK_INSUFFICIENT);
        }
        if (result == 1) {
            throw new BusinessException(ExceptionEnum.USER_REPEAT_SECOND_KILL);
        }

        // 3. 发送消息到 RabbitMQ（异步创建订单）
        SeckillOrderMessage message = new SeckillOrderMessage(userId, seckillGoodsId, quantity);


        rabbitTemplate.convertAndSend("seckill.exchange", "seckill.order", message);

        // 4. 返回一个临时订单号或提示，前端轮询最终结果
        return "订单生成成功,请排队等候";


    }
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





}