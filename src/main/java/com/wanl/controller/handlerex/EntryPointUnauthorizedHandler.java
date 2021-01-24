/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.controller.handlerex;

import com.wanl.constant.ImunityConstant;
import com.wanl.entity.Result;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class EntryPointUnauthorizedHandler implements AuthenticationEntryPoint {
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException {
        if (request.getHeader(ImunityConstant.X_REQUEST_WITH) != null
                && request.getHeader(ImunityConstant.X_REQUEST_WITH).equalsIgnoreCase(ImunityConstant.XML_HTTP_REQUEST)) {
            response.setCharacterEncoding(ImunityConstant.UTF_8);
            response.setContentType(ImunityConstant.CONTENT_TYPE_APP_JSON_UTF_8);
            PrintWriter writer = response.getWriter();
            System.out.println(authException.getMessage());
            String unauthorized = Result.failed(Integer.valueOf(401), "对不起!token无效!");
            writer.write(unauthorized);
            writer.close();
        } else {
            response.sendRedirect(request.getContextPath() + "/admin/login");
        }
    }
}
