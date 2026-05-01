package com.mall.LongTou.service.Imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.LongTou.common.BusinessException;
import com.mall.LongTou.common.ExceptionEnum;
import com.mall.LongTou.mapper.ProductMapper;
import com.mall.LongTou.mapper.ShoppingCartMapper;
import com.mall.LongTou.entity.Product;
import com.mall.LongTou.entity.ShoppingCart;
import com.mall.LongTou.service.ShoppingCartService;
import com.mall.LongTou.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Auther: wdd
 * @Date: 2020-03-19 13:22
 * @Description:
 */
@Service
public class ShoppingCartServiceImp extends ServiceImpl<ShoppingCartMapper,ShoppingCart> implements ShoppingCartService {

        @Autowired
        private ShoppingCartMapper cartMapper;
        @Autowired
        private ProductMapper productMapper;

        @Override
        @Transactional(rollbackFor = Exception.class)
        public void addToCart(Integer userId, Integer productId, Integer num) {
                if (userId == null || productId == null || num == null || num <= 0) {
                        throw new BusinessException(ExceptionEnum.PARAM_ERROR, "参数错误：用户ID、商品ID或数量不合法");
                }

                // 查询是否已存在该商品
                LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(ShoppingCart::getUserId, userId)
                        .eq(ShoppingCart::getProductId, productId);
                ShoppingCart exist = this.getOne(wrapper);

                if (exist != null) {
                        // 已存在，数量相加
                        exist.setNum(exist.getNum() + num);
                        this.updateById(exist);
                } else {
                        // 不存在，新增
                        ShoppingCart cart = new ShoppingCart();
                        cart.setUserId(userId);
                        cart.setProductId(productId);
                        cart.setNum(num);
                        this.save(cart);
                }
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public void removeFromCart(Integer userId, Integer productId) {
                if (userId == null || productId == null) {
                        throw new BusinessException(ExceptionEnum.PARAM_ERROR, "用户ID或商品ID不能为空");
                }

                LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(ShoppingCart::getUserId, userId)
                        .eq(ShoppingCart::getProductId, productId);
                boolean removed = this.remove(wrapper);
                if (!removed) {
                        throw new BusinessException(ExceptionEnum.PRODUCT_NOT_IN_CART);
                }
        }

        @Override
        public List<ShoppingCart> getUserCart(Integer userId) {
                if (userId == null) {
                        throw new BusinessException(ExceptionEnum.USER_CANTOT_NULL);
                }
                LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(ShoppingCart::getUserId, userId)
                        .orderByDesc(ShoppingCart::getId);
                List<ShoppingCart> cartList = this.list(wrapper);
                // 为空返回空列表，不抛异常（前端自行处理）
                return cartList;
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public void updateCartNum(Integer userId, Integer productId, Integer num) {
                if (userId == null || productId == null || num == null) {
                        throw new BusinessException(ExceptionEnum.PARAM_ERROR, "参数不合法");
                }
                if (num < 0) {
                        throw new BusinessException(ExceptionEnum.PARAM_ERROR, "数量不能为负数");
                }

                LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(ShoppingCart::getUserId, userId)
                        .eq(ShoppingCart::getProductId, productId);
                ShoppingCart cart = this.getOne(wrapper);
                if (cart == null) {
                        throw new BusinessException(ExceptionEnum.PRODUCT_NOT_IN_CART);
                }

                if (num == 0) {
                        // 数量为0则删除
                        this.removeById(cart.getId());
                } else {
                        cart.setNum(num);
                        this.updateById(cart);
                }
        }
}
