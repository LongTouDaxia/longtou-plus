package com.mall.xiaomi.controller;

import com.mall.xiaomi.dto.UserDTO;
import com.mall.xiaomi.entity.User;
import com.mall.xiaomi.service.Imp.UserServiceImp;
import com.mall.xiaomi.service.UserService;
import com.mall.xiaomi.util.BeanUtil;
import com.mall.xiaomi.util.CookieUtil;
import com.mall.xiaomi.util.MD5Util;
import com.mall.xiaomi.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: wdd
 * @Date: 2020-03-19 13:27
 * @Description:
 */
@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * @param userDTO
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO) {


        String token = userService.login(userDTO);

        return Result.success("登陆成功",token);
    }

    /**
     * 用户注册
     * @param userDTO
     * @return
     */
    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDTO) {
        String token = userService.register(userDTO);
        return Result.success("注册成功");
    }





}
