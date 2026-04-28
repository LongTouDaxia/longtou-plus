package com.mall.xiaomi.service.Imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.xiaomi.exception.ExceptionEnum;
import com.mall.xiaomi.exception.XmException;
import com.mall.xiaomi.mapper.CarouselMapper;
import com.mall.xiaomi.entity.Carousel;
import com.mall.xiaomi.service.CarouselService;
import org.apache.commons.lang3.ArrayUtils;
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
        List<Carousel> list = null;
        try {
            list = carouselMapper.selectAll();
            if (ArrayUtils.isEmpty(list.toArray())) {
                throw new XmException(ExceptionEnum.GET_CAROUSEL_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new XmException(ExceptionEnum.GET_CAROUSEL_ERROR);
        }
        return list;
    }

}
