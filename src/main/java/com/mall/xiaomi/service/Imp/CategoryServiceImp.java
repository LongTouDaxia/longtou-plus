package com.mall.xiaomi.service.Imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.xiaomi.common.ExceptionEnum;
import com.mall.xiaomi.common.XmException;
import com.mall.xiaomi.mapper.CategoryMapper;
import com.mall.xiaomi.entity.Category;
import com.mall.xiaomi.service.CategoryService;
import com.mall.xiaomi.util.Result;
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

    public Result getAll() {
        List<Category> categories = categoryMapper.selectList(null);
        if(categories == null){
            return Result.error(ExceptionEnum.CATEGORY_NOT_FIND.getMessage());
        }
        return Result.success(categories);

    }
}
