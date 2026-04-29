package com.mall.xiaomi.config;

import com.mall.xiaomi.Interceptor.JwtInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //添加jwt拦截器
        registry.addInterceptor(new JwtInterceptor())
                .addPathPatterns("/**");//添加拦截路径
    }
}
