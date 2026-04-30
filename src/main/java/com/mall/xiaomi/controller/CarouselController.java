package com.mall.xiaomi.controller;

import com.mall.xiaomi.entity.Carousel;
import com.mall.xiaomi.service.CarouselService;
import com.mall.xiaomi.service.Imp.CarouselServiceImp;
import com.mall.xiaomi.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Auther: wdd
 * @Date: 2020-03-19 13:24
 * @Description:
 */
@RestController
public class CarouselController {


    @Autowired
    private CarouselService carouselService;

    @GetMapping("/resources/carousel")
    public Result carousels() {

        return carouselService.getCarouselList();
    }

}
