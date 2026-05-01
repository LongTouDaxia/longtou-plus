package com.mall.LongTou.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.LongTou.entity.Product;
import com.mall.LongTou.common.Result;
import com.mall.LongTou.entity.ProductPicture;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public interface ProductService extends IService<Product> {

    List<Product> getProductList(Integer categoryId, Integer page, Integer pageSize);

    Product getProductDetail(@NotNull(message = "商品ID不能为空") Integer productId);

    List<ProductPicture> getProductPictures(@NotNull(message = "商品ID不能为空") Integer productId);

    void addProductPicture(@NotNull(message = "商品ID不能为空") Integer productId, @NotBlank(message = "图片URL不能为空") String pictureUrl);

    void deleteProductPicture(@NotNull(message = "图片ID不能为空") Integer pictureId);
}
