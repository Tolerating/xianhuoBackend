package com.xianhuo.xianhuobackend.config;

import com.xianhuo.xianhuobackend.interceptor.LoginInterceptor;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/api/login", "/img/**", "/api/alipay/notify", "/api/register", "/api/mailCode", "/api/updatePassword","/api/admin/login");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);
        String path = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\productImg\\";
        registry.addResourceHandler("/img/**").addResourceLocations("file:" + path);
    }
}
