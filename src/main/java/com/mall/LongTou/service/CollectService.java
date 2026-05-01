package com.mall.LongTou.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.LongTou.entity.Collect;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public interface CollectService extends IService<Collect> {
    
    void addCollect(@NotNull(message = "用户ID不能为空") Integer userId, @NotNull(message = "商品ID不能为空") Integer productId);

    void removeCollect(@NotNull(message = "用户ID不能为空") Integer userId, @NotNull(message = "商品ID不能为空") Integer productId);

    List<Collect> getUserCollects();
}
