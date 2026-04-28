package com.mall.xiaomi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mall.xiaomi.entity.SeckillTime;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Auther: wdd
 * @Date: 2020-04-22 20:58
 * @Description:
 */

@Mapper
public interface SeckillTimeMapper extends BaseMapper<SeckillTime> {

        @Select("select * from seckill_time where end_time > #{time} limit 6")
        List<SeckillTime> getTime(long time);

        @Delete("delete from seckill_time")
        void deleteAll();

        @Select("select end_time from seckill_time where time_id = #{timeId}")
        Long getEndTime(Integer timeId);
}
