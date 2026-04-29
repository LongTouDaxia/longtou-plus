package com.mall.xiaomi.service.Imp;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.xiaomi.exception.ExceptionEnum;
import com.mall.xiaomi.exception.XmException;
import com.mall.xiaomi.mapper.ProductPictureMapper;
import com.mall.xiaomi.entity.ProductPicture;
import com.mall.xiaomi.service.ProductPictureService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: wdd
 * @Date: 2020-03-19 13:22
 * @Description:
 */
@Service
public class ProductPictureServiceImp extends ServiceImpl<ProductPictureMapper,ProductPicture> implements ProductPictureService {

    @Autowired
    private ProductPictureMapper productPictureMapper;

    public List<ProductPicture> getProductPictureByProductId(String productId) {

        List<ProductPicture> productPictureList = query()
                .eq("product_id", productId)
                .list();
      //  List<ProductPicture> productPictureList = (List<ProductPicture>) productPictureMapper.selectById(Integer.parseInt(productId));
        if(CollectionUtils.isEmpty(productPictureList)){
            throw new XmException(ExceptionEnum.GET_CAROUSEL_NOT_FOUND);
        }
        return productPictureList;
    }
}
