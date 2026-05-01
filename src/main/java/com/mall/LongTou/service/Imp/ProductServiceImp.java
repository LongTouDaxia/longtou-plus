package com.mall.LongTou.service.Imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.LongTou.common.BusinessException;
import com.mall.LongTou.common.ExceptionEnum;
import com.mall.LongTou.entity.ProductPicture;
import com.mall.LongTou.mapper.ProductMapper;
import com.mall.LongTou.entity.Product;
import com.mall.LongTou.mapper.ProductPictureMapper;
import com.mall.LongTou.service.ProductService;
import com.mall.LongTou.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Auther: wdd
 * @Date: 2020-03-19 13:21
 * @Description:
 */
@Service
public class ProductServiceImp extends ServiceImpl<ProductMapper,Product> implements ProductService {

        @Autowired
        private ProductMapper productMapper;

        @Autowired
        private ProductPictureMapper productPictureMapper;

        @Override
        public List<Product> getProductList(Integer categoryId, Integer page, Integer pageSize) {
                // 分页查询，page 和 pageSize 默认为 1 和 20（由 Controller 层默认值保证）
                LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
                if (categoryId != null && categoryId > 0) {
                        wrapper.eq(Product::getCategoryId, categoryId);
                }
                // 按销量排序（或者按创建时间，这里按 product_id 倒序）
                wrapper.orderByDesc(Product::getProductId);
                Page<Product> pageResult = productMapper.selectPage(new Page<>(page, pageSize), wrapper);
                return pageResult.getRecords();
        }

        @Override
        public Product getProductDetail(Integer productId) {
                if (productId == null) {
                        throw new BusinessException(ExceptionEnum.PRODUCTID_NOT_NULL);
                }
                Product product = productMapper.selectById(productId);
                if (product == null) {
                        throw new BusinessException(ExceptionEnum.PRODUCT_NOT_FOUND);
                }
                return product;
        }

        @Override
        public List<ProductPicture> getProductPictures(Integer productId) {
                if (productId == null) {
                        throw new BusinessException(ExceptionEnum.PRODUCTID_NOT_NULL);
                }
                LambdaQueryWrapper<ProductPicture> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(ProductPicture::getProductId, productId)
                        .orderByAsc(ProductPicture::getId);
                return productPictureMapper.selectList(wrapper);
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public void addProductPicture(Integer productId, String pictureUrl) {
                if (productId == null || pictureUrl == null || pictureUrl.trim().isEmpty()) {
                        throw new BusinessException(ExceptionEnum.PRODUCTID_NOT_NULL, "商品ID或图片URL不能为空");
                }
                // 检查商品是否存在
                Product product = productMapper.selectById(productId);
                if (product == null) {
                        throw new BusinessException(ExceptionEnum.PRODUCT_NOT_FOUND);
                }
                ProductPicture picture = new ProductPicture();
                picture.setProductId(productId);
                picture.setProductPicture(pictureUrl);
                // intro 字段可选，这里设为空或从参数传入
                picture.setIntro(null);
                int rows = productPictureMapper.insert(picture);
                if (rows != 1) {
                        throw new BusinessException(ExceptionEnum.PRODUCT_DELETE_EXCEPTION, "添加图片失败");
                }
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public void deleteProductPicture(Integer pictureId) {
                if (pictureId == null) {
                        throw new BusinessException(ExceptionEnum.PRODUCTID_NOT_NULL, "图片ID不能为空");
                }
                // 可选：检查图片是否存在
                ProductPicture picture = productPictureMapper.selectById(pictureId);
                if (picture == null) {
                        throw new BusinessException(ExceptionEnum.PRODUCT_NOT_FOUND, "图片不存在");
                }
                int rows = productPictureMapper.deleteById(pictureId);
                if (rows != 1) {
                        throw new BusinessException(ExceptionEnum.PRODUCT_DELETE_EXCEPTION, "删除图片失败");
                }
        }
}
