package com.mall.xiaomi.service.Imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.xiaomi.common.ExceptionEnum;
import com.mall.xiaomi.common.XmException;
import com.mall.xiaomi.mapper.ProductMapper;
import com.mall.xiaomi.mapper.ShoppingCartMapper;
import com.mall.xiaomi.entity.Product;
import com.mall.xiaomi.entity.ShoppingCart;
import com.mall.xiaomi.service.ShoppingCartService;
import com.mall.xiaomi.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

        public List<CartVo> getCartByUserId(String userId) {
                ShoppingCart cart = new ShoppingCart();
                cart.setUserId(Integer.parseInt(userId));
                List<ShoppingCart> list = null;
                List<CartVo> cartVoList = new ArrayList<>();

                // 1. 查询购物车数据
                List<ShoppingCart> cartList = cartMapper.selectList(
                        new LambdaQueryWrapper<ShoppingCart>()
                                .eq(ShoppingCart::getUserId, userId)
                );

                // 2. 将 ShoppingCart 转换为 CartVo
                if (CollectionUtils.isNotEmpty(cartList)) {
                        for (ShoppingCart c : cartList) {

                                cartVoList.add(getCartVo(c));
                        }
                }
                return cartVoList;
        }

        @Transactional
        public CartVo addCart(String productId, String userId) {
                ShoppingCart cart = new ShoppingCart();
                cart.setUserId(Integer.parseInt(userId));
                cart.setProductId(Integer.parseInt(productId));
                // 查看数据库是否已存在,存在数量直接加1
                ShoppingCart shoppingCart = query().eq("productId", productId)
                        .eq("userId", userId)
                        .one();
             //   ShoppingCart one = cartMapper.selectOne(cart);

                if (shoppingCart != null) {

                        // 还要判断是否达到该商品规定上限
                        if (shoppingCart.getNum() >= 5) { // TODO 这里默认设为5 后期再动态修改
                                throw new XmException(ExceptionEnum.CATEGORY_PRODUCT_NULL);
                        }
                        Integer version = shoppingCart.getVersion();
                        // 尝试更新数量并增加版本号
                        shoppingCart.setNum(shoppingCart.getNum() + 1);

                        // 使用带有版本号判断的条件更新
                        int updateCount = cartMapper.updateCartByIdAndVersion(shoppingCart);

                        if (updateCount == 0) {
                                CartVo cartVo = new CartVo();

                                cartVo.setUpdateMessage("并发修改失败，请重试！");
                                cartVo.setUpdateNum(false);
                                return cartVo;
                        } else {
                                return null;
                        }
                } else {
                        // 不存在
                        cart.setNum(1);
                        cart.setVersion(1);
                        cartMapper.insert(cart);
                        return getCartVo(cart);
                }
        }

        /**
         * 封装类
         *
         * @param cart
         * @return
         */
        private CartVo getCartVo(ShoppingCart cart) {
                // 获取商品，用于封装下面的类
                Product product = productMapper.selectById(cart.getProductId());
                // 返回购物车详情
                CartVo cartVo = new CartVo();
                cartVo.setId(cart.getId());
                cartVo.setProductId(cart.getProductId());
                cartVo.setProductName(product.getProductName());
                cartVo.setProductImg(product.getProductPicture());
                cartVo.setPrice(product.getProductSellingPrice());
                cartVo.setNum(cart.getNum());
                cartVo.setMaxNum(5); // TODO 这里默认设为5 后期再动态修改
                cartVo.setCheck(false);
                return cartVo;
        }

        public void updateCartNum(String cartId, String userId, String num) {
                ShoppingCart cart = new ShoppingCart();
                cart.setId(Integer.parseInt(cartId));
                cart.setUserId(Integer.parseInt(userId));
                cart.setNum(Integer.parseInt(num));
                try {
                        //增加数量
                        boolean flag = save(cart);
                        if (flag) {
                                throw new XmException(ExceptionEnum.ORDER_CREATE_EXCEPTION);
                        }
                } catch (Exception e) {
                        e.printStackTrace();
                        throw new XmException(ExceptionEnum.ORDER_CREATE_EXCEPTION);
                }
        }

        public void deleteCart(String cartId, String userId) {
                ShoppingCart cart = new ShoppingCart();
                cart.setId(Integer.parseInt(cartId));
                cart.setUserId(Integer.parseInt(userId));
                try {
                        cartMapper.deleteById(cart);
                } catch (XmException e) {
                        e.printStackTrace();
                        throw new XmException(ExceptionEnum.GET_CAROUSEL_NOT_FOUND);
                }
        }
}
