package com.mall.xiaomi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.xiaomi.dto.UserDTO;
import com.mall.xiaomi.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService extends IService<User> {

    String login(UserDTO userDTO);
    String register(UserDTO userDTO);
}
