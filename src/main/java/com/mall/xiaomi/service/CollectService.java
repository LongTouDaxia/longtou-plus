package com.mall.xiaomi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.xiaomi.entity.Collect;
import com.mall.xiaomi.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CollectService extends IService<Collect> {
    void addCollect(String userId, String productId);

    List<Product> getCollect(String userId);

    void deleteCollect(String userId, String productId);
}
