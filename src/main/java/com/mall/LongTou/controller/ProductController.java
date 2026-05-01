package com.mall.LongTou.controller;

import com.mall.LongTou.common.Result;
import com.mall.LongTou.entity.Product;
import com.mall.LongTou.entity.ProductPicture;
import com.mall.LongTou.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/product")
@Validated
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/list")
    public Result<List<Product>> getProductList(
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        List<Product> list = productService.getProductList(categoryId, page, pageSize);
        return Result.success(list);
    }

    @GetMapping("/detail/{productId}")
    public Result<Product> getProductDetail(@PathVariable @NotNull(message = "商品ID不能为空") Integer productId) {
        Product product = productService.getProductDetail(productId);
        return Result.success(product);
    }




    //主要我感觉也没必要多写一个controller

    // 商品图片子资源（如果 ProductPictureController 单独存在，这里可以不写；但保留方便）
    @GetMapping("/{productId}/pictures")
    public Result<List<ProductPicture>> getProductPictures(@PathVariable @NotNull(message = "商品ID不能为空") Integer productId) {
        List<ProductPicture> pictures = productService.getProductPictures(productId);
        return Result.success(pictures);
    }

    @PostMapping("/{productId}/pictures")
    public Result<Void> addProductPicture(@PathVariable @NotNull(message = "商品ID不能为空") Integer productId,
                                          @RequestParam @NotBlank(message = "图片URL不能为空") String pictureUrl) {
        productService.addProductPicture(productId, pictureUrl);
        return Result.success();
    }

    @DeleteMapping("/pictures/{pictureId}")
    public Result<Void> deleteProductPicture(@PathVariable @NotNull(message = "图片ID不能为空") Integer pictureId) {
        productService.deleteProductPicture(pictureId);
        return Result.success();
    }
}