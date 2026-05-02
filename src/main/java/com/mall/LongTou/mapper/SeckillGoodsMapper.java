package com.mall.LongTou.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.LongTou.entity.SeckillGoods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SeckillGoodsMapper extends BaseMapper<SeckillGoods> {

    //回滚数据库库存
    @Update("update seckill_goods set seckill_stock = seckill_stock + #{productNum}  " +
            "where seckill_goods_id = #{seckillGoodsId}")
    void incrstock(Integer seckillGoodsId, Integer productNum);
}
