/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.filter.qq;

import com.wanl.entity.OpenUser;
import com.wanl.entity.Permission;
import com.wanl.entity.QQUser;
import com.wanl.entity.Role;
import com.wanl.entity.User;
import com.wanl.filter.qq.QQAuthenticationFilter;
import com.wanl.filter.qq.QQAuthenticationManager;
import com.wanl.mapper.PermissionMapper;
import com.wanl.service.UserService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class QQAuthenticationManager implements AuthenticationManager {
    private static final List<GrantedAuthority> AUTHORITIES = new ArrayList<GrantedAuthority>();
    private UserService userService;
    private PermissionMapper permissionMapper;

    public QQAuthenticationManager(UserService userService, PermissionMapper permissionMapper) {
        this.userService = userService;
        this.permissionMapper = permissionMapper;
    }

    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        if (auth.getName() != null && auth.getCredentials() != null) {

            QQUser qqUser = QQAuthenticationFilter.getUserInfo(auth.getName(), (String) auth.getCredentials());

            User user = this.userService.getUserByOpenId((String) auth.getCredentials(), "QQ");
            if (user != null && user.getRoles() != null && qqUser != null) {
                for (Role role : user.getRoles()) {
                    AUTHORITIES.add(new SimpleGrantedAuthority(role.getName()));
                }
                OpenUser openUserByOpenIdAndUserId = this.userService
                        .getOpenUserByOpenIdAndUserId((String) auth.getCredentials(), user.getId(), "QQ");
                openUserByOpenIdAndUserId.setNickname(qqUser.getNickname());
                openUserByOpenIdAndUserId.setAvatar(qqUser.getAvatar());
                this.userService.updateOpenUser(openUserByOpenIdAndUserId);
                user.setAuthorities(AUTHORITIES);

                List<Permission> permissions = this.permissionMapper.findByAdminUserId(user.getId().intValue());
                for (Permission permission : permissions) {
                    if (permission != null && permission.getName() != null) {
                        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(
                                permission.getName());
                        AUTHORITIES.add(simpleGrantedAuthority);
                    }
                }
                return new UsernamePasswordAuthenticationToken(user, auth.getCredentials(), AUTHORITIES);
            }
            throw new UsernameNotFoundException("账户未绑定!");
        }
        throw new BadCredentialsException("坏的凭据!");
    }
}
