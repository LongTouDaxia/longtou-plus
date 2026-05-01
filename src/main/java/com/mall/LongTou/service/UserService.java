package com.mall.LongTou.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.LongTou.dto.UserRegisterDTO;
import com.mall.LongTou.entity.User;
import org.hibernate.validator.constraints.Length;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Service
public interface UserService extends IService<User> {


    String register(@Valid UserRegisterDTO userRegisterDTO);

    String login(@NotBlank(message = "用户名不能为空") @Length(min = 4, max = 20, message = "用户名长度4-20位") String username, @NotBlank(message = "密码不能为空") @Length(min = 6, max = 32, message = "密码长度6-32位") String password);
}
