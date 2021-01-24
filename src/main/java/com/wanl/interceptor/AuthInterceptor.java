/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.interceptor;

import com.wanl.entity.EsmUser;
import com.wanl.entity.Result;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class AuthInterceptor implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession();
        EsmUser user = (EsmUser) session.getAttribute("USER_SESSION");
        if (user != null) {
            return true;
        }
        if (request.getHeader("X-Requested-With") != null
                && request.getHeader("X-Requested-With").equalsIgnoreCase("XMLHttpRequest")) {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json;charset=utf-8");
            PrintWriter writer = response.getWriter();
            String unauthorized = Result.failed(401, "请登录!");
            writer.write(unauthorized);
            writer.close();
            return false;
        }
        response.sendRedirect(request.getContextPath() + "/login");
        return false;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }
}
