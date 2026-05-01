package com.mall.LongTou.common;

import com.mall.LongTou.dto.UserDTO;
import com.mall.LongTou.dto.UserLoginDTO;
import org.springframework.stereotype.Component;

@Component
public class UserHolder {



    // 线程局部变量，每个线程独立存储一份 UserDTO
    private static final ThreadLocal<UserDTO> THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 保存当前登录用户信息到当前线程
     */
    public void save(UserDTO userDTO) {
        THREAD_LOCAL.set(userDTO);
    }

    /**
     * 获取当前线程中的用户信息
     */
    public UserDTO getUser() {
        return THREAD_LOCAL.get();
    }

    /**
     * 清除当前线程中的用户信息（防止内存泄漏）
     * 必须在请求结束后（如拦截器的 afterCompletion 或过滤器的 finally 中）调用
     */
    public void clear() {
        THREAD_LOCAL.remove();
    }
}
