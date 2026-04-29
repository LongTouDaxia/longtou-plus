package com.mall.xiaomi.service.Imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.xiaomi.exception.ExceptionEnum;
import com.mall.xiaomi.exception.XmException;
import com.mall.xiaomi.mapper.ProductMapper;
import com.mall.xiaomi.entity.Product;
import com.mall.xiaomi.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        public List<Product> getProductByCategoryId(Integer categoryId) {
                // 创建分页对象，查询第1页，每页8条
             //   Page<Product> page = new Page<>(1, 8);

                // 创建查询条件
                LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(Product::getCategoryId, categoryId)
                        .orderByDesc(Product::getProductSales);

                try {
                        // 执行分页查询
                       // Page<Product> result = productMapper.selectPage(page, queryWrapper);

                        List<Product> productList = productMapper.selectList(queryWrapper);


                        if (CollectionUtils.isEmpty(productList)) {
                                throw new XmException(ExceptionEnum.GET_PRODUCT_NOT_FOUND);
                        }

                        return productList;
                } catch (Exception e) {
                        e.printStackTrace();
                        throw new XmException(ExceptionEnum.GET_PRODUCT_ERROR);
                }
        }

        public List<Product> getHotProduct() {
            //    Page<Product>  page = new Page<>(1, 8);

                LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.orderByDesc(Product::getProductSales);

                try {
                       // Page<Product> result = productMapper.selectPage(page, queryWrapper);

                        List<Product> productList = productMapper.selectList(queryWrapper);
                        if(CollectionUtils.isEmpty(productList)){
                                throw new XmException(ExceptionEnum.GET_PRODUCT_NOT_FOUND);
                        }
                        return productList;
                }catch (Exception e){
                        e.printStackTrace();
                        throw new XmException(ExceptionEnum.GET_PRODUCT_ERROR);
                }
        }

        public Product getProductById(String productId) {
                Product product = productMapper.selectById(productId);
            //    Product product = null;
                if (product == null) {
                        throw new XmException(ExceptionEnum.GET_PRODUCT_NOT_FOUND);
                }
                return product;
        }

        public Page<Product> getProductByPage(String currentPage, String pageSize, String categoryId) {
                // 参数转换
                int pageNum = Integer.parseInt(currentPage);
                int size = Integer.parseInt(pageSize);

                // 创建分页对象
                Page<Product> page = new Page<>(pageNum, size);

                // 创建查询条件
                QueryWrapper<Product> queryWrapper = new QueryWrapper<>();

                if (!"0".equals(categoryId)) {
                        // 按分类查询
                        queryWrapper.eq("category_id", Integer.parseInt(categoryId));
                }

                try {
                        // 执行分页查询
                        Page<Product> result = productMapper.selectPage(page, queryWrapper);

                        // MyBatis-Plus 的 Page 对象已经包含了分页信息
                        return result;

                } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException("分页查询商品失败");
                }
        }
}
