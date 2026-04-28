package com.mall.xiaomi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.xiaomi.entity.Order;
import com.mall.xiaomi.vo.OrderVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    @Select("select `order`.*, `order`.order_time as orderTime ,product.product_name as productName, product.product_picture as productPicture " +
            "from `order`, product where `order`.product_id = product.product_id and `order`.user_id = #{userId} " +
            "ORDER BY order_time desc"
    )
    List<OrderVo> getOrderVoByUserId(Integer userId);
}
