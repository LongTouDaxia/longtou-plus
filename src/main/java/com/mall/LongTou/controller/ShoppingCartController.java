package com.mall.LongTou.controller;

import com.mall.LongTou.common.ExceptionEnum;
import com.mall.LongTou.common.Result;
import com.mall.LongTou.dto.CartDTO;
import com.mall.LongTou.entity.ShoppingCart;
import com.mall.LongTou.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/cart")
@Validated
public class ShoppingCartController {

        @Autowired
        private ShoppingCartService shoppingCartService;

        @PostMapping("/add")
        public Result<Void> addToCart(@Valid @RequestBody CartDTO dto) {
                shoppingCartService.addToCart(dto.getUserId(), dto.getProductId(), dto.getNum());
                return Result.success();
        }

        @DeleteMapping("/remove")
        public Result<Void> removeFromCart(@Valid @RequestBody CartDTO dto) {
                shoppingCartService.removeFromCart(dto.getUserId(), dto.getProductId());
                return Result.success();
        }

        @GetMapping("/list")
        public Result<List<ShoppingCart>> getUserCart(@RequestParam Integer userId) {
                if(userId == null){
                        return Result.error(ExceptionEnum.USER_CANTOT_NULL);
                }
                List<ShoppingCart> list = shoppingCartService.getUserCart(userId);
                return Result.success(list);
        }

        @PutMapping("/update")
        public Result<Void> updateCartNum(@Valid @RequestBody CartDTO dto) {
                shoppingCartService.updateCartNum(dto.getUserId(), dto.getProductId(), dto.getNum());
                return Result.success();
        }
}