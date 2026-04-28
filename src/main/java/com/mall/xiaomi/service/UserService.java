package com.mall.xiaomi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.xiaomi.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService extends IService<User> {
    User login(User user);

    void register(User user);

    void isUserName(String username);
}
