package com.pos_magistral.app.configuracion;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class web_config implements WebMvcConfigurer {

    private final rol_interceptor rolInterceptor;

    public web_config(rol_interceptor rolInterceptor) {
        this.rolInterceptor = rolInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rolInterceptor)
                .addPathPatterns("/mesero/**", "/cajero/**", "/admin/**", "/cliente/**");
    }
}