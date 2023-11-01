package com.mizore.mob.config;

import com.mizore.mob.interceptor.LoginCheckInterceptor;
import com.mizore.mob.interceptor.RefreshTokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RefreshTokenInterceptor());

        registry.addInterceptor(new LoginCheckInterceptor())
                .excludePathPatterns(
                "/user/login",
                "/user/sign",
                "/user/code"
        );
    }
}
