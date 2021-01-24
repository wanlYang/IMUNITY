/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.configuration;

import com.wanl.configuration.SecurityConfig;
import com.wanl.constant.ImunityConstant;
import com.wanl.controller.handlerex.EntryPointUnauthorizedHandler;
//import com.wanl.controller.handlerex.ImunityAccessDeniedHandler;
import com.wanl.entity.Result;
import com.wanl.filter.KaptchaAuthenticationFilter;
import com.wanl.filter.qq.QQAuthenticationFilter;
import com.wanl.filter.qq.QQAuthenticationManager;
import com.wanl.mapper.PermissionMapper;
import com.wanl.redis.RedisCacheManager;
import com.wanl.security.CustomUserServiceImpl;
import com.wanl.security.ImunityFilterSecurityInterceptor;
import com.wanl.service.UserService;
import com.wanl.utils.MD5PasswordEncoder;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration

public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private SessionRegistry sessionRegistry;
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private RedisCacheManager redisCacheManager;
    @Autowired
    private ImunityFilterSecurityInterceptor imunityFilterSecurityInterceptor;
    //@Autowired
    //private ImunityAccessDeniedHandler imunityAccessDeniedHandler;
    @Autowired
    private EntryPointUnauthorizedHandler unauthorizedHandler;
    @Autowired
    private UserService userService;

    @Bean
    public UserDetailsService costomUserService() {
        return new CustomUserServiceImpl();
    }

    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(costomUserService()).passwordEncoder(new MD5PasswordEncoder());
    }

    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/admin/static/**", "/admin/uploads/**", "/community/**",
                "/productimg/**", "/static/**");
    }

    @Bean
    public SessionRegistry getSessionRegistry() {
        return new SessionRegistryImpl();
    }

    protected void configure(HttpSecurity http) throws Exception {
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8");
        encodingFilter.setForceEncoding(true);
        http.cors()//新加入
                .and()
                .csrf().disable();
        http.addFilterBefore(encodingFilter,CsrfFilter.class);
        http.addFilterBefore(this.imunityFilterSecurityInterceptor,FilterSecurityInterceptor.class);
        http.addFilterAt(qqAuthenticationFilter(),UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(kaptchaAuthenticationFilter(),UsernamePasswordAuthenticationFilter.class);
        http.headers().frameOptions().sameOrigin().and().authorizeRequests()
            .antMatchers(new String[] { "/admin/sessiontimeout", "/admin/open/oauth2/qq" }).permitAll()
            .antMatchers(new String[] { "/admin/**" }).access("hasRole('ROLE_ADMIN') or hasRole('ROLE_SUPER_ADMIN')")
            .anyRequest().permitAll().and().formLogin().loginPage("/admin/login")
            .loginProcessingUrl("/admin/login/submit")
            .usernameParameter("admin_name")
            .passwordParameter("admin_password")
            .successHandler(new AuthenticationSuccessHandler() {
                @Override
                public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                        Authentication authentication) throws IOException, ServletException {
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter writer = response.getWriter();
                    writer.write("{\"status\":\"ok\",\"message\":\"登录成功\"}");
                    writer.close();
                }
            })
            .failureHandler(new AuthenticationFailureHandler() {
                
                @Override
                public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                        AuthenticationException exception) throws IOException, ServletException {
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter writer = response.getWriter();
                    writer.write("{\"status\":\"error\",\"message\":\"登录失败\"}");
                    writer.close();
                    
                }
            }).and()
            .exceptionHandling()
            //.accessDeniedHandler(this.imunityAccessDeniedHandler)
            .authenticationEntryPoint(this.unauthorizedHandler)
            .and().logout().permitAll()
            .logoutSuccessUrl("/admin/login")
            .logoutRequestMatcher(new AntPathRequestMatcher("/admin/logout","POST"))
            .invalidateHttpSession(true)
            .clearAuthentication(true)
            .deleteCookies(new String[] { "JSESSIONID" })
            .and().sessionManagement()
            .sessionFixation()
            .migrateSession()
            .invalidSessionUrl("/admin/sessiontimeout")
            .sessionAuthenticationErrorUrl("/admin/sessiontimeout")
            .maximumSessions(1)
            .expiredUrl("/admin/login")
            .maxSessionsPreventsLogin(true)
            .sessionRegistry(this.sessionRegistry);
    }

    private QQAuthenticationFilter qqAuthenticationFilter() {
        QQAuthenticationFilter authenticationFilter = new QQAuthenticationFilter("/admin/qq/oauth2/login",
                this.userService);
        SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
        successHandler.setAlwaysUseDefaultTargetUrl(true);
        successHandler.setDefaultTargetUrl("/admin/manager/index");
        authenticationFilter
                .setAuthenticationManager(new QQAuthenticationManager(this.userService, this.permissionMapper));
        authenticationFilter.setAuthenticationSuccessHandler(successHandler);
        authenticationFilter.setAuthenticationFailureHandler(new AuthenticationFailureHandler() {
            @Override
            public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
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
        return authenticationFilter;
    }

    public KaptchaAuthenticationFilter kaptchaAuthenticationFilter() throws Exception {
        return new KaptchaAuthenticationFilter("/admin/login/submit", this.redisCacheManager);
    }
}
