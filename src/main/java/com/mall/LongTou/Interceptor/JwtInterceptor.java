package com.mall.LongTou.Interceptor;

import com.mall.LongTou.common.UserHolder;
import com.mall.LongTou.dto.UserDTO;
import com.mall.LongTou.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserHolder userHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 从请求头获取 token
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // 2. 验证 token 是否存在且有效
        if (token == null || !jwtUtil.validateToken(token)) {
            //无效就拦截
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print("{\"code\":401,\"message\":\"未登录或token已过期\"}");
            out.flush();
            return false;

        }

        // 3. 从 token 中提取用户信息并存入 ThreadLocal
        String userId = jwtUtil.getUserIdFromToken(token);
        String username = jwtUtil.getUsernameFromToken(token);

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userId);      // 根据你的 DTO 字段名调整
        userDTO.setUserName(username);
        userHolder.save(userDTO);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 请求结束后清除 ThreadLocal，防止内存泄漏
        userHolder.clear();
    }
}