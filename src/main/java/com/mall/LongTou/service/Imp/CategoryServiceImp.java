package com.mall.LongTou.service.Imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.LongTou.common.BusinessException;
import com.mall.LongTou.common.ExceptionEnum;
import com.mall.LongTou.mapper.CategoryMapper;
import com.mall.LongTou.entity.Category;
import com.mall.LongTou.service.CategoryService;
import com.mall.LongTou.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: wdd
 * @Date: 2020-03-19 13:21
 * @Description:
 */
@Service
public class CategoryServiceImp extends ServiceImpl<CategoryMapper,Category> implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public List<Category> getAll() {

        List<Category> categories = categoryMapper.selectList(null);
        if(categories == null){
            throw new BusinessException(ExceptionEnum.CATEGORY_NOT_FIND);
        }
        return categories;

    }
}
