package com.mall.LongTou.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.LongTou.entity.Carousel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CarouselService extends IService<Carousel> {
    List<Carousel> getCarouselList();
}
