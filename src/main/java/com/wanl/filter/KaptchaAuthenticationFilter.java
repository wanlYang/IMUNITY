/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.filter;

import com.wanl.constant.ImunityConstant;
import com.wanl.entity.Result;
import com.wanl.filter.KaptchaAuthenticationFilter;
import com.wanl.redis.RedisCacheManager;
import com.wanl.utils.CookieUtil;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class KaptchaAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private String servletPath;
    private RedisCacheManager redisCacheManager;

    public KaptchaAuthenticationFilter(String servletPath, RedisCacheManager redisCacheManager) {
        super(servletPath);
        this.redisCacheManager = redisCacheManager;
        this.servletPath = servletPath;
        setAuthenticationFailureHandler(new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                    AuthenticationException exception) throws IOException, ServletException {
                if (request.getHeader(ImunityConstant.X_REQUEST_WITH) != null
                        && request.getHeader(ImunityConstant.X_REQUEST_WITH).equalsIgnoreCase(ImunityConstant.XML_HTTP_REQUEST)) {
                    response.setCharacterEncoding(ImunityConstant.UTF_8);
                    response.setContentType(ImunityConstant.CONTENT_TYPE_APP_JSON_UTF_8);
                    PrintWriter writer = response.getWriter();
                    String unauthorized = Result.failed(Integer.valueOf(403), exception.getMessage());
                    writer.write(unauthorized);
                    writer.close();
                } else {
                    response.sendRedirect(request.getContextPath() + "/403");
                }
                
            }
        });
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if ("POST".equalsIgnoreCase(request.getMethod()) && this.servletPath.equals(request.getServletPath())) {
            String uid = CookieUtil.getCookie(request, "adminVerifyCode", false);
            if (uid == null) {
                unsuccessfulAuthentication(request, response,
                        new InsufficientAuthenticationException("验证码可能已经过期!请重试!"));
                return;
            }
            String code = (String) this.redisCacheManager.get(uid);
            if (code == null) {
                unsuccessfulAuthentication(request, response,
                        new InsufficientAuthenticationException("验证码可能已经过期!请重试!"));
                return;
            }
            if (!code.equalsIgnoreCase(request.getParameter("adminVerifyCode"))) {
                unsuccessfulAuthentication(request, response, new InsufficientAuthenticationException("验证码输入不正确!"));
                return;
            }
            this.redisCacheManager.del(new String[] { uid });
        }
        chain.doFilter(request, response);
    }

    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        return null;
    }
}
