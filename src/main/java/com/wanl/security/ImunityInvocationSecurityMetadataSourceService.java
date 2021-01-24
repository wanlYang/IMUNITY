/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.security;

import com.wanl.entity.Permission;
import com.wanl.mapper.PermissionMapper;
import com.wanl.security.ImunityInvocationSecurityMetadataSourceService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Service;

@Service
public class ImunityInvocationSecurityMetadataSourceService implements FilterInvocationSecurityMetadataSource {
    @Autowired
    private PermissionMapper permissionMapper;
    private HashMap<String, Collection<ConfigAttribute>> map = null;

    public void loadResourceDefine() {
        this.map = new HashMap<String, Collection<ConfigAttribute>>();

        List<Permission> permissions = this.permissionMapper.findAll();
        for (Permission permission : permissions) {
            Collection<ConfigAttribute> array = new ArrayList<ConfigAttribute>();
            SecurityConfig securityConfig = new SecurityConfig(permission.getName());

            array.add(securityConfig);

            this.map.put(permission.getUrl(), array);
        }
    }

    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        if (this.map == null)
            loadResourceDefine();

        HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();

        for (Iterator<String> iter = this.map.keySet().iterator(); iter.hasNext();) {
            String resUrl = (String) iter.next();
            AntPathRequestMatcher matcher = new AntPathRequestMatcher(resUrl);
            if (matcher.matches(request)) {
                return (Collection<ConfigAttribute>) this.map.get(resUrl);
            }
        }
        return null;
    }

    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    public boolean supports(Class<?> clazz) {
        return true;
    }
}
