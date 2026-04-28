package com.mall.xiaomi.controller;

import com.mall.xiaomi.entity.ProductPicture;
import com.mall.xiaomi.service.Imp.ProductPictureServiceImp;
import com.mall.xiaomi.service.ProductPictureService;
import com.mall.xiaomi.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Auther: wdd
 * @Date: 2020-03-19 13:27
 * @Description:
 */
@RestController
@RequestMapping("/productPicture")
public class ProductPictureController {


    @Autowired
    private ProductPictureService productPictureService;

    @GetMapping("/product/{productId}")
    public Result productPicture(@PathVariable String productId) {
        List<ProductPicture> products = productPictureService.getProductPictureByProductId(productId);
        return Result.success(products);
    }

}
