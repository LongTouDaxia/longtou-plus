package com.mall.xiaomi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.xiaomi.entity.ProductPicture;
import com.mall.xiaomi.util.Result;
import org.springframework.stereotype.Service;

@Service
public interface ProductPictureService extends IService<ProductPicture> {
    Result getProductPictureByProductId(String productId);
}
