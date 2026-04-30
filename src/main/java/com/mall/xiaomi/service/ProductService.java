package com.mall.xiaomi.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.xiaomi.entity.Product;
import com.mall.xiaomi.util.Result;
import org.springframework.stereotype.Service;

@Service
public interface ProductService extends IService<Product> {
    Result getProductByCategoryId(Integer categoryId);

    Result getHotProduct();

    Result getProductById(String productId);

    Page<Product> getProductByPage(String currentPage, String pageSize, String categoryId);
}
