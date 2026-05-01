package com.mall.LongTou.controller;

import com.mall.LongTou.common.Result;
import com.mall.LongTou.dto.UserLoginDTO;
import com.mall.LongTou.dto.UserRegisterDTO;
import com.mall.LongTou.entity.User;
import com.mall.LongTou.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result<String> register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        String token = userService.register(userRegisterDTO);
        return Result.success(token);
    }

    @PostMapping("/login")
    public Result<String> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        String token = userService.login(userLoginDTO.getUsername(), userLoginDTO.getPassword());
        return Result.success(token);
    }


}