package com.project.pos.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class PosInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        Object currentPos = (session != null) ? session.getAttribute("currentPos") : null;

        String uri = request.getRequestURI();
        if (uri.startsWith("/order")) {
            if (currentPos == null) {
                response.sendRedirect("/home/page1");
                return false;
            }
        }

        return true;
    }
}
