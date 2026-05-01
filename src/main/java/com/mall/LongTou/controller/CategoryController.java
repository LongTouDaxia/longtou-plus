package com.mall.LongTou.controller;

import com.mall.LongTou.common.Result;
import com.mall.LongTou.entity.Category;
import com.mall.LongTou.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public Result<List<Category>> getAllCategories() {

        List<Category> list = categoryService.getAll();
        return Result.success(list);
    }
}