package com.project.pos.config;

import com.project.pos.config.interceptor.LoginInterceptor;
import com.project.pos.config.interceptor.PosInterceptor;
import com.project.pos.config.interceptor.StoreInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/login", "/api/login", "/logout", "/icon/**", "/js/**", "/styles/**");

        registry.addInterceptor(new StoreInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/login", "/api/login",  "/logout", "/store", "/store/affiliation", "/store/new-store", "/api/saveStore", "/home/selectStore", "/icon/**", "/js/**", "/styles/**");

        registry.addInterceptor(new PosInterceptor())
                .addPathPatterns("/order");
    }
}
