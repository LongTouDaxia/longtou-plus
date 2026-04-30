package com.mall.xiaomi.service.Imp;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.xiaomi.common.ExceptionEnum;
import com.mall.xiaomi.common.XmException;
import com.mall.xiaomi.mapper.CollectMapper;
import com.mall.xiaomi.entity.Collect;
import com.mall.xiaomi.service.CollectService;
import com.mall.xiaomi.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


/**
 * @Auther: wdd
 * @Date: 2020-03-19 13:21
 * @Description:
 */
@Service
public class CollectServiceimp extends ServiceImpl<CollectMapper,Collect> implements CollectService {

    @Autowired
    private CollectMapper collectMapper;

    @Transactional
    public Result addCollect(String userId, String productId) {
        //非空判断
        if(userId==null||productId==null){
            return Result.error(ExceptionEnum.USER_OR_PRODUCTID_IS_NULL.getMessage());
        }

        // 先看看是否数据库中已存在
        Collect collect = query().eq("product_id", Integer.parseInt(productId))
                .eq("user_id", Integer.parseInt(userId))
                .one();
        if(collect!=null){
            //商品已收藏
            return Result.error(ExceptionEnum.PRODUCT_COLLECTED.getMessage());
        }

        // 不存在，添加收藏
        Collect newCollect = new Collect();
        newCollect.setUserId(Integer.parseInt(userId));
        newCollect.setProductId(Integer.parseInt(productId));
        collect.setCollectTime(new Date().getTime());
        save(collect);
        return Result.success("商品收藏成功");
    }

    public Result getCollect(String userId) {
        if(userId == null){
            //用户为空
            return Result.error(ExceptionEnum.USER_CANTOT_NULL.getMessage());
        }

        //查询用户收藏
        List<Collect> collect = query().eq("user_id", Integer.parseInt(userId))
                .list();
        //收藏为空
        if(CollectionUtils.isEmpty(collect)){
            return Result.error(ExceptionEnum.COLLECT_IS_NULL.getMessage());
        }
        return Result.success(collect);
    }

    @Transactional
    public Result deleteCollect(String userId, String productId) {
        if(productId == null){
            return Result.error(ExceptionEnum.PRODUCTID_NOT_NULL.getMessage());
        }
        if(userId == null){
            //用户为空
            return Result.error(ExceptionEnum.USER_CANTOT_NULL.getMessage());
        }
        //查询商品是否存在收藏
        Collect collect = query().eq("user_id", Integer.parseInt(userId))
                .eq("product_id", Integer.parseInt(productId)).one();
        if(collect==null){
            //返回商品未收藏
            return Result.error(ExceptionEnum.PRODUCT_NOT_COLLECT.getMessage());
        }
        try {
            collectMapper.deleteById(collect.getId());
            return Result.success("商品删除成功");
        }catch (RuntimeException e){
            return Result.error(ExceptionEnum.PRODUCT_DELETE_EXCEPTION.getMessage());
        }

    }
}
