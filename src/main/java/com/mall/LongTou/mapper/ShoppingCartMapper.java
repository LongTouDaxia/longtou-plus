package com.mall.LongTou.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.LongTou.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {

        int updateCartByIdAndVersion(@Param("cart") ShoppingCart cart);
}
