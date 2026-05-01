package com.mall.LongTou.service.Imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.LongTou.common.BusinessException;
import com.mall.LongTou.common.ExceptionEnum;
import com.mall.LongTou.entity.Collect;
import com.mall.LongTou.mapper.CollectMapper;
import com.mall.LongTou.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CollectServiceImpl extends ServiceImpl<CollectMapper,Collect> implements CollectService {

    @Autowired
    private CollectMapper collectMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCollect(Integer userId, Integer productId) {
        // 参数校验（理论上 Controller 已经用 @Valid 校验过，但防御性编程）
        if (userId == null || productId == null) {
            throw new BusinessException(ExceptionEnum.USER_OR_PRODUCTID_IS_NULL);
        }

        // 检查是否已收藏
        LambdaQueryWrapper<Collect> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Collect::getUserId, userId)
               .eq(Collect::getProductId, productId);
        Long count = collectMapper.selectCount(wrapper);
        if (count > 0) {
            throw new BusinessException(ExceptionEnum.PRODUCT_COLLECTED);
        }

        // 插入收藏记录
        Collect collect = new Collect();
        collect.setUserId(userId);
        collect.setProductId(productId);
        collect.setCollectTime(System.currentTimeMillis());
        int rows = collectMapper.insert(collect);
        if (rows != 1) {
            throw new BusinessException(ExceptionEnum.ADD_ORDER_ERROR, "收藏失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeCollect(Integer userId, Integer productId) {
        if (userId == null || productId == null) {
            throw new BusinessException(ExceptionEnum.USER_OR_PRODUCTID_IS_NULL);
        }

        // 检查是否存在
        LambdaQueryWrapper<Collect> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Collect::getUserId, userId)
               .eq(Collect::getProductId, productId);
        Collect exist = collectMapper.selectOne(wrapper);
        if (exist == null) {
            throw new BusinessException(ExceptionEnum.PRODUCT_NOT_COLLECT);
        }

        // 删除
        int rows = collectMapper.deleteById(exist.getId());
        if (rows != 1) {
            throw new BusinessException(ExceptionEnum.PRODUCT_DELETE_EXCEPTION, "取消收藏失败");
        }
    }

    @Override
    public List<Collect> getUserCollects(Integer userId) {
        if (userId == null) {
            throw new BusinessException(ExceptionEnum.USER_CANTOT_NULL);
        }
        LambdaQueryWrapper<Collect> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Collect::getUserId, userId)
               .orderByDesc(Collect::getCollectTime);
        List<Collect> list = collectMapper.selectList(wrapper);
        if (list == null || list.isEmpty()) {
            throw new BusinessException(ExceptionEnum.COLLECT_IS_NULL);
        }
        return list;
    }
}