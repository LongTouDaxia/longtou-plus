package com.mall.xiaomi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.xiaomi.entity.ShoppingCart;
import com.mall.xiaomi.vo.CartVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ShoppingCartService extends IService<ShoppingCart> {
    List<CartVo> getCartByUserId(String userId);

    CartVo addCart(String productId, String userId);

    void updateCartNum(String cartId, String userId, String num);

    void deleteCart(String cartId, String userId);
}
