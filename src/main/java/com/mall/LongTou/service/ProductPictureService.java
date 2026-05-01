package com.mall.LongTou.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.LongTou.entity.ProductPicture;
import com.mall.LongTou.common.Result;
import org.springframework.stereotype.Service;

@Service
public interface ProductPictureService extends IService<ProductPicture> {
    Result getProductPictureByProductId(String productId);
}
