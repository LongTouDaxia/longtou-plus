// 实现
package com.mall.LongTou.service.Imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.LongTou.entity.SeckillActivity;
import com.mall.LongTou.mapper.SeckillActivityMapper;
import com.mall.LongTou.service.SeckillActivityService;
import com.mall.LongTou.vo.SeckillActivityVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeckillActivityServiceImpl extends ServiceImpl<SeckillActivityMapper, SeckillActivity> 
        implements SeckillActivityService {

    @Autowired
    private SeckillActivityMapper seckillActivityMapper;
    @Override
    public List<SeckillActivityVO> getCurrentActivities() {
        long now = System.currentTimeMillis();
        LambdaQueryWrapper<SeckillActivity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SeckillActivity::getStatus, 1)
                .le(SeckillActivity::getStartTime, now)
                .ge(SeckillActivity::getEndTime, now);
        List<SeckillActivity> activities = seckillActivityMapper.selectList(wrapper);
        return activities.stream().map(activity -> {
            SeckillActivityVO vo = new SeckillActivityVO();
            BeanUtils.copyProperties(activity, vo);
            return vo;
        }).collect(Collectors.toList());
    }
}