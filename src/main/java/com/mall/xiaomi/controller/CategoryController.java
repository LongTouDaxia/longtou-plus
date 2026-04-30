package com.mall.xiaomi.controller;

import com.mall.xiaomi.entity.Category;
import com.mall.xiaomi.service.CategoryService;
import com.mall.xiaomi.service.Imp.CategoryServiceImp;
import com.mall.xiaomi.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Auther: wdd
 * @Date: 2020-03-19 13:25
 * @Description:
 */
@RestController
@RequestMapping("/category")
public class CategoryController {


    @Autowired
    private CategoryService categoryService;

    @GetMapping("")
    public Result category() {

        return categoryService.getAll();
    }

}
