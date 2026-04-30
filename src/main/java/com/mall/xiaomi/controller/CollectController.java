package com.mall.xiaomi.controller;

import com.mall.xiaomi.entity.Product;
import com.mall.xiaomi.service.CollectService;
import com.mall.xiaomi.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Auther: wdd
 * @Date: 2020-03-19 13:25
 * @Description:
 */
@RestController
@RequestMapping("/collect")
public class CollectController {

    @Autowired
    private CollectService collectService;

    /**
     * 将商品收藏
     * @param userId
     * @param productId
     * @return
     */
    @PostMapping("/user/{productId}/{userId}")
    public Result addCollect(@PathVariable String userId, @PathVariable String productId) {

        return collectService.addCollect(userId, productId);
    }

    /**
     * 获取用户收藏
     * @param userId
     * @return 返回商品集合
     */
    @GetMapping("/user/{userId}")
    public Result getCollect(@PathVariable String userId) {

        return collectService.getCollect(userId);
    }

    @DeleteMapping("/user/{productId}/{userId}")
    public Result deleteCollect(@PathVariable String productId, @PathVariable String userId) {

        return collectService.deleteCollect(userId, productId);
    }
}
