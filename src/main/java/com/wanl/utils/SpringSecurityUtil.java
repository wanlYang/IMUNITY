/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.utils;

import com.wanl.constant.ImunityConstant;
import com.wanl.entity.Permission;
import com.wanl.entity.Role;
import com.wanl.entity.User;
import com.wanl.mapper.PermissionMapper;
import com.wanl.service.UserService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class SpringSecurityUtil {
    public static Authentication getSecurityAuthentication() {
        return getSecurityContextImpl().getAuthentication();
    }

    public static void updateSecurityContextImplAuthentication(User user) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        Authentication authentication = getSecurityAuthentication();
        List<Permission> permissions = ((PermissionMapper) SpringContextUtil.getBean(PermissionMapper.class))
                .findByAdminUserId(user.getId().intValue());
        if (user != null && user.getId() != null) {
            User findUser = ((UserService) SpringContextUtil.getBean(UserService.class)).getUser(user.getId());
            boolean conOne = (StringUtils.isNotBlank(user.getUsername())
                    && StringUtils.isNoneBlank(new CharSequence[] { findUser.getUsername() })
                    && !user.getUsername().equals(findUser.getUsername()));
            if (conOne) {
                findUser.setUsername(user.getUsername());
            }
            boolean conTwo = (StringUtils.isNotBlank(user.getBindemail())
                    && StringUtils.isNoneBlank(new CharSequence[] { findUser.getBindemail() })
                    && !user.getBindemail().equals(findUser.getBindemail()));
            if (conTwo) {
                findUser.setBindemail(user.getBindemail());
            }
            boolean conThree = (StringUtils.isNotBlank(user.getSex())
                    && StringUtils.isNoneBlank(new CharSequence[] { findUser.getSex() })
                    && !user.getSex().equals(findUser.getSex()));
            if (conThree) {
                findUser.setSex(user.getSex());
            }
            boolean conFour = (StringUtils.isNotBlank(user.getAge())
                    && StringUtils.isNoneBlank(new CharSequence[] { findUser.getAge() })
                    && !user.getAge().equals(findUser.getAge()));
            if (conFour) {
                findUser.setAge(user.getAge());
            }
            boolean conFive = (StringUtils.isNotBlank(user.getPhone())
                    && StringUtils.isNoneBlank(new CharSequence[] { findUser.getPhone() })
                    && !user.getPhone().equals(findUser.getPhone()));
            if (conFive) {
                findUser.setPhone(user.getPhone());
            }
            boolean conSix = (user.getBirthday() != null && findUser.getBirthday() != null
                    && !DateUtils.isSameDay(user.getBirthday(), findUser.getBirthday()));
            if (conSix) {
                findUser.setBirthday(user.getBirthday());
            }
            boolean conSeven = (StringUtils.isNotBlank(user.getProfile())
                    && StringUtils.isNoneBlank(new CharSequence[] { findUser.getProfile() })
                    && !user.getProfile().equals(findUser.getProfile()));
            if (conSeven) {
                findUser.setProfile(user.getProfile());
            }
            boolean conEig = (StringUtils.isNotBlank(user.getHeadImg())
                    && StringUtils.isNoneBlank(new CharSequence[] { findUser.getHeadImg() })
                    && !user.getHeadImg().equals(findUser.getHeadImg()));
            if (conEig) {
                findUser.setHeadImg(user.getHeadImg());
            }
            if (findUser.getRoles() != null && findUser.getRoles().size() > 0) {
                for (Role role : findUser.getRoles()) {
                    authorities.add(new SimpleGrantedAuthority(role.getName()));
                    for (Permission permission : permissions) {
                        if (permission != null && permission.getName() != null) {
                            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(
                                    permission.getName());
                            authorities.add(simpleGrantedAuthority);
                        }
                    }
                }
                findUser.setAuthorities(authorities);
            }

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(findUser,
                    authentication.getCredentials(), authorities);
            auth.setDetails(authentication.getDetails());

            getSecurityContextImpl().setAuthentication(auth);
        }
    }

    public static Collection<? extends GrantedAuthority> getUserAuthorities() {
        return getSecurityContextImpl().getAuthentication().getAuthorities();
    }

    private static SecurityContextImpl getSecurityContextImpl() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        //HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference("request");

        HttpSession session = (HttpSession) requestAttributes.resolveReference("session");

        return (SecurityContextImpl) session.getAttribute(ImunityConstant.SPRING_SECURITY_CONTEXT);
    }
}
