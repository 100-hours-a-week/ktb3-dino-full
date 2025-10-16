// config/WebConfig.java
package com.example.spring_practice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final AuthInterceptor auth;
    public WebConfig(AuthInterceptor auth){ this.auth = auth; }

    @Override public void addInterceptors(InterceptorRegistry r){
        r.addInterceptor(auth).addPathPatterns("/api/**");
    }
}
