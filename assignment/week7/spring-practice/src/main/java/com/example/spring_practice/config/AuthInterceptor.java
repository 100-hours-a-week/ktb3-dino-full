package com.example.spring_practice.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    private boolean whitelisted(HttpServletRequest req){
        String uri = req.getRequestURI();
        return uri.equals("/api/login")
                || uri.equals("/api/join")
                || uri.startsWith("/h2-console/")
                || uri.startsWith("/swagger-ui/")
                || uri.startsWith("/v3/api-docs/");
    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) return true; // CORS preflight
        if (whitelisted(req)) return true;

        HttpSession session = req.getSession(false);
        if (session == null) { res.setStatus(401); return false; }

        Object u = session.getAttribute("LOGIN_USER_ID");
        if (u == null) { res.setStatus(401); return false; }
        return true;
    }

}
