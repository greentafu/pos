package com.project.pos.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

public class StoreInterceptor implements HandlerInterceptor {
    private static final List<String> allowedPaths
            = List.of("/login", "/api/login", "/logout", "/store", "/store/affiliation", "/store/new-store", "/api/saveStore", "/home/selectStore");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("loginUser") != null
                && session.getAttribute("currentStore") == null
                && !allowedPaths.contains(request.getRequestURI())) {

            response.sendRedirect("/store/affiliation");
            return false;
        }
        return true;
    }
}
