package com.mall.xiaomi.service.Imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.xiaomi.common.ExceptionEnum;
import com.mall.xiaomi.mapper.CarouselMapper;
import com.mall.xiaomi.entity.Carousel;
import com.mall.xiaomi.service.CarouselService;
import com.mall.xiaomi.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: wdd
 * @Date: 2020-03-19 13:16
 * @Description:
 */
@Service
public class CarouselServiceImp extends ServiceImpl<CarouselMapper,Carousel> implements CarouselService {

    @Autowired
    private CarouselMapper carouselMapper;

    public Result getCarouselList() {
        List<Carousel> carousels = carouselMapper.selectList(null);
        if(carousels == null){
            return Result.error(ExceptionEnum.GET_CAROUSEL_NOT_FOUND.getMessage());
        }
        return Result.success(carousels);
    }

}
