package com.mall.xiaomi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.xiaomi.entity.Collect;
import com.mall.xiaomi.util.Result;
import org.springframework.stereotype.Service;

@Service
public interface CollectService extends IService<Collect> {
    Result addCollect(String userId, String productId);

    Result getCollect(String userId);

    Result deleteCollect(String userId, String productId);
}
