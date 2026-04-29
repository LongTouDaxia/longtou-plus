package com.mall.xiaomi.service.Imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.xiaomi.dto.UserDTO;
import com.mall.xiaomi.exception.ExceptionEnum;
import com.mall.xiaomi.exception.XmException;
import com.mall.xiaomi.mapper.UserMapper;
import com.mall.xiaomi.entity.User;
import com.mall.xiaomi.service.UserService;
import com.mall.xiaomi.util.JwtUtil;
import com.mall.xiaomi.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Auther: wdd
 * @Date: 2020-03-19 13:23
 * @Description:
 */
@Service
public class UserServiceImp extends ServiceImpl<UserMapper,User> implements UserService {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserMapper userMapper;


    @Override
    public String login(UserDTO userDTO) {
        //数据库查询用户信息
        User user = query().eq("username", userDTO.getUserName())
                .eq("password", MD5Util.MD5Encode(userDTO.getPassword(), "UTF-8"))
                .one();

        if(user==null){
            throw new XmException(ExceptionEnum.GET_USER_NOT_FOUND);
        }

        //生成并返回token令牌
        return jwtUtil.createToken(user.getUsername(),user.getUserId().toString());
    }


    public String register(UserDTO userDTO) {

        User user = new User();
        user.setUsername(user.getUsername());
        // 先去看看用户名是否重复

        //其实用户名重复了我感觉也没啥
        Long count = query().eq("username", user.getUsername()).count();

        if (count > 0) {
            // 用户名已存在
            throw new XmException(ExceptionEnum.SAVE_USER_REUSE);
        }
        // 使用md5对密码进行加密
        user.setPassword(MD5Util.MD5Encode(user.getPassword() + "", "UTF-8"));
        // 存入数据库
        save(user);
        //这也不是啥高并发  加锁也有点夸张
        //获取token
        String token = jwtUtil.createToken(user.getUsername(), user.getUserId().toString());
        return token;
    }


}
