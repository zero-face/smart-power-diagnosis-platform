package com.zero.smart_power_diagnosis_platform.config;

import com.zero.smart_power_diagnosis_platform.interceptor.JWTInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author Zero
 * @Date 2021/5/10 19:12
 * @Since 1.8
 **/

@Configuration
public class LoginWebConfig implements WebMvcConfigurer {
    @Autowired
    private JWTInterceptor jwtInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(jwtInterceptor);
//                .addPathPatterns("/**")
//                .excludePathPatterns("/smart_power_diagnosis_platform/user-info/login",
//                        "/smart_power_diagnosis_platform/user-info/code",
//                        "/swagger-resources/**",
//                        "/webjars/**",
//                        "/v2/**",
//                        "/swagger-ui.html",
//                        "/error",
//                        "/smart_power_diagnosis_platform/ele-site/**",
//                        "/smart_power_diagnosis_platform/transfrom/getwarntrans");
    }
}
