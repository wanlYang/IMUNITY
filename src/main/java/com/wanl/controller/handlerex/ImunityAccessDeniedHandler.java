/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.controller.handlerex;

import com.wanl.constant.ImunityConstant;
import com.wanl.entity.Result;
import com.wanl.utils.CookieUtil;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class ImunityAccessDeniedHandler implements AccessDeniedHandler {
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        CookieUtil.delCookie(response, request, ImunityConstant.ADMIN_HEAD_IMG);
        CookieUtil.delCookie(response, request, ImunityConstant.QQ_OAUTH2_SUCCESS);
        if (request.getHeader(ImunityConstant.X_REQUEST_WITH) != null
                && request.getHeader(ImunityConstant.X_REQUEST_WITH).equalsIgnoreCase(ImunityConstant.XML_HTTP_REQUEST)) {
            response.setCharacterEncoding(ImunityConstant.UTF_8);
            response.setContentType(ImunityConstant.CONTENT_TYPE_APP_JSON_UTF_8);
            PrintWriter writer = response.getWriter();
            String unauthorized = Result.failed(Integer.valueOf(403), "对不起!无权限访问!");
            writer.write(unauthorized);
            writer.close();
        } else {
            response.sendRedirect(request.getContextPath() + "/403");
        }
    }
}
