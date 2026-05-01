package com.mall.LongTou.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.LongTou.entity.ShoppingCart;
import com.mall.LongTou.vo.CartVo;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public interface ShoppingCartService extends IService<ShoppingCart> {

    void addToCart(@NotNull(message = "用户ID不能为空") Integer userId, @NotNull(message = "商品ID不能为空") Integer productId, @NotNull(message = "数量不能为空") @Min(value = 1, message = "数量至少为1") @Max(value = 999, message = "数量不能超过999") Integer num);

    void removeFromCart(@NotNull(message = "用户ID不能为空") Integer userId, @NotNull(message = "商品ID不能为空") Integer productId);

    List<ShoppingCart> getUserCart(Integer userId);

    void updateCartNum(@NotNull(message = "用户ID不能为空") Integer userId, @NotNull(message = "商品ID不能为空") Integer productId, @NotNull(message = "数量不能为空") @Min(value = 1, message = "数量至少为1") @Max(value = 999, message = "数量不能超过999") Integer num);
}
