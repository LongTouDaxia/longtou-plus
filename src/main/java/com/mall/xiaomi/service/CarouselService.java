package com.mall.xiaomi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.xiaomi.entity.Carousel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CarouselService extends IService<Carousel> {
    List<Carousel> getCarouselList();
}
