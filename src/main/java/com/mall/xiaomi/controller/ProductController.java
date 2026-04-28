package com.mall.xiaomi.controller;

import com.github.pagehelper.PageInfo;
import com.mall.xiaomi.entity.Product;
import com.mall.xiaomi.service.Imp.ProductServiceImp;
import com.mall.xiaomi.service.ProductService;
import com.mall.xiaomi.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: wdd
 * @Date: 2020-03-19 13:26
 * @Description:
 */
@RestController
@RequestMapping("/product")
public class ProductController {


    @Autowired
    private ProductService productService;

    @GetMapping("/category/limit/{categoryId}")
    public Result getProductByCategoryId(@PathVariable Integer categoryId) {
        List<Product> list = productService.getProductByCategoryId(categoryId);
        return Result.success(list);

    }

    @GetMapping("/category/hot")
    public Result getHotProduct() {
        List<Product> list = productService.getHotProduct();
        return Result.success(list);

    }

    @GetMapping("/{productId}")
    public Result getProduct(@PathVariable String productId) {
        Product product = productService.getProductById(productId);
        return Result.success(product);
    }

    @GetMapping("/page/{currentPage}/{pageSize}/{categoryId}")
    public Result getProductByPage(@PathVariable String currentPage, @PathVariable String pageSize, @PathVariable String categoryId) {
        PageInfo<Product> pageInfo = productService.getProductByPage(currentPage, pageSize, categoryId);
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", "001");
        map.put("data", pageInfo.getList());
        map.put("total", pageInfo.getTotal());
        return Result.success(map);
    }

}
