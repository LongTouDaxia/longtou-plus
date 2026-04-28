package com.mall.xiaomi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.xiaomi.entity.ProductPicture;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductPictureService extends IService<ProductPicture> {
    List<ProductPicture> getProductPictureByProductId(String productId);
}
