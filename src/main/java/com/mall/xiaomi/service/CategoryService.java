package com.mall.xiaomi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.xiaomi.entity.Category;
import com.mall.xiaomi.util.Result;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService extends IService<Category> {
    Result getAll();
}
