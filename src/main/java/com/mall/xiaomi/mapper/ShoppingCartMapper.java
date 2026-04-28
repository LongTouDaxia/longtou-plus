package com.mall.xiaomi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.xiaomi.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {

        int updateCartByIdAndVersion(@Param("cart") ShoppingCart cart);
}
