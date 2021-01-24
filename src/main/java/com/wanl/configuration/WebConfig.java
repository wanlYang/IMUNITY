/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.configuration;

import com.wanl.interceptor.AuthInterceptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig  implements WebMvcConfigurer{
    private static final Logger logger = LogManager.getLogger(WebConfig.class.getName());
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/404").setViewName("admin/404");
        registry.addViewController("/500").setViewName("admin/500");
        registry.addViewController("/403").setViewName("admin/403");
    }
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(new AuthInterceptor());
        interceptorRegistration.excludePathPatterns("/login", "/regist");
        interceptorRegistration
                .addPathPatterns("/shopcart/**", "/order/**", "/review/**", "/account/**");
    }


}
