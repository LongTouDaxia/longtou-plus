package com.mall.LongTou.service.Imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.LongTou.common.BusinessException;
import com.mall.LongTou.common.ExceptionEnum;
import com.mall.LongTou.mapper.CarouselMapper;
import com.mall.LongTou.entity.Carousel;
import com.mall.LongTou.service.CarouselService;
import com.mall.LongTou.common.Result;
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

    public List<Carousel> getCarouselList() {
        List<Carousel> carousels = carouselMapper.selectList(null);
        if(carousels == null){
            throw new BusinessException(ExceptionEnum.GET_CAROUSEL_NOT_FOUND);
        }
        return carousels;
    }

}
