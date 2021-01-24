/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.security;

import java.util.Collection;
import java.util.Iterator;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class ImunityAccessDecisionManager implements AccessDecisionManager {
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
            throws AccessDeniedException, InsufficientAuthenticationException {
        if (null == configAttributes || configAttributes.size() <= 0) {
            return;
        }

        for (Iterator<ConfigAttribute> iter = configAttributes.iterator(); iter.hasNext();) {
            ConfigAttribute c = iter.next();
            String needRole = c.getAttribute();
            for (GrantedAuthority ga : authentication.getAuthorities()) {
                if (needRole.trim().equals(ga.getAuthority())) {
                    return;
                }
            }
        }

        throw new AccessDeniedException("no right");
    }

    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    public boolean supports(Class<?> clazz) {
        return true;
    }
}
