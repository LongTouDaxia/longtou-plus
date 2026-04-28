package com.mall.xiaomi.controller;

import com.mall.xiaomi.service.Imp.ShoppingCartServiceImp;
import com.mall.xiaomi.service.ShoppingCartService;
import com.mall.xiaomi.util.Result;
import com.mall.xiaomi.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: wdd
 * @Date: 2020-03-19 13:27
 * @Description:
 */
@RestController
@RequestMapping("/cart")
public class ShoppingCartController {


        @Autowired
        private ShoppingCartService shoppingCartService;

        /**
         * 获取购物车信息
         *
         * @param userId
         * @return
         */
        @GetMapping("/user/{userId}")
        public Result cart(@PathVariable String userId) {
                List<CartVo> carts = shoppingCartService.getCartByUserId(userId);
                return Result.success(carts);
        }

        /**
         * 添加购物车
         *
         * @param productId
         * @param userId
         * @return
         */
        @PostMapping("/product/user/{productId}/{userId}")
        public Result cart(@PathVariable String productId, @PathVariable String userId) {
                CartVo cartVo = shoppingCartService.addCart(productId, userId);

                if (cartVo != null) {

                        if (cartVo.isUpdateNum()) {
                                Result.success( "添加购物车成功", cartVo.getUpdateMessage());
                        }
                        Result.success("添加购物车成功", cartVo);
                } else {
                        Result.success("该商品已经在购物车，数量+1");
                }
                return Result.success("添加成功");
        }

        @PutMapping("/user/num/{cartId}/{userId}/{num}")
        public Result cart(@PathVariable String cartId, @PathVariable String userId, @PathVariable String num) {
                shoppingCartService.updateCartNum(cartId, userId, num);
                return Result.success("更新成功");
        }

        @DeleteMapping("/user/{cartId}/{userId}")
        public Result deleteCart(@PathVariable String cartId, @PathVariable String userId) {
                shoppingCartService.deleteCart(cartId, userId);
                return Result.success("删除成功");
        }
}
