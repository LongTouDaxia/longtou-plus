package com.mall.LongTou.service.Imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.LongTou.common.BusinessException;
import com.mall.LongTou.common.ExceptionEnum;
import com.mall.LongTou.dto.UserRegisterDTO;
import com.mall.LongTou.mapper.UserMapper;
import com.mall.LongTou.entity.User;
import com.mall.LongTou.service.UserService;
import com.mall.LongTou.util.JwtUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public String register(UserRegisterDTO userRegisterDTO) {
        //属性赋值
        User user = new User();
        BeanUtils.copyProperties(userRegisterDTO, user);

        userMapper.insert(user);
        //生成token
        String token = jwtUtil.createToken(user.getUsername(), String.valueOf(user.getUserId()));
        if(token == null)
            throw new BusinessException(ExceptionEnum.TOKEN_CREATE_ERROR);
        return token;


    }

    @Override
    public String login(String username, String password) {
        //查数据库
        User user = query().eq("username", username).eq("password", password).one();
        if(user == null){
            throw new BusinessException(ExceptionEnum.USER_NOT_FOUND);
        }

        //存在  则生成token
        String token = jwtUtil.createToken(user.getUsername(), String.valueOf(user.getUserId()));
        if(token == null)
            throw new BusinessException(ExceptionEnum.TOKEN_CREATE_ERROR);
        return token;


    }
}
