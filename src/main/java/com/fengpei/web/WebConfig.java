package com.fengpei.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 对所有路径应用CORS策略
                .allowedOrigins("http://localhost:8082","http://47.109.33.172:8082") // 允许的源
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 允许的方法
                .allowedHeaders("*") // 允许的头部
                .allowCredentials(true) // 是否发送Cookie等凭证信息
                .maxAge(3600); // 预检请求的有效期（秒）
    }
}
