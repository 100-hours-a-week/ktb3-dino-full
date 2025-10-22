// config/AuthInterceptor.java
package com.example.spring_practice.config;

import com.example.spring_practice.dto.UserDto;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    private boolean whitelisted(HttpServletRequest req){
        String uri = req.getRequestURI();
        return uri.equals("/api/join") || uri.equals("/api/login");
    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        if (whitelisted(req)) return true;
        UserDto u = (UserDto) req.getSession().getAttribute("loginUser");
        if (u == null) { res.setStatus(401); return false; }
        return true;
    }
}
