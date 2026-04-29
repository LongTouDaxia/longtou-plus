package com.mall.xiaomi.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.xiaomi.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService extends IService<Product> {
    List<Product> getProductByCategoryId(Integer categoryId);

    List<Product> getHotProduct();

    Product getProductById(String productId);

    Page<Product> getProductByPage(String currentPage, String pageSize, String categoryId);
}
