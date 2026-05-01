package com.mall.LongTou.config;

import com.mall.LongTou.Interceptor.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加jwt拦截器
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/collect/**",
                        "/cart/**",
                        "/order/**");
               //添加拦截路径   购物车和收藏商品需要拦截

    }
}
