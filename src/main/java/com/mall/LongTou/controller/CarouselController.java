package com.mall.LongTou.controller;

import com.mall.LongTou.common.Result;
import com.mall.LongTou.entity.Carousel;
import com.mall.LongTou.service.CarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/carousel")
public class CarouselController {

    @Autowired
    private CarouselService carouselService;

    @GetMapping
    public Result<List<Carousel>> getCarouselList() {
        List<Carousel> list = carouselService.getCarouselList();
        return Result.success(list);
    }
}