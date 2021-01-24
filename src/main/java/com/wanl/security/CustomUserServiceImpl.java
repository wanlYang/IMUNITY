/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.security;

import com.wanl.entity.Permission;
import com.wanl.entity.Role;
import com.wanl.entity.User;
import com.wanl.mapper.PermissionMapper;
import com.wanl.security.CustomUserServiceImpl;
import com.wanl.service.UserService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserServiceImpl implements UserDetailsService {
    @Autowired
    private UserService userService;
    @Autowired
    private PermissionMapper permissionMapper;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userService.getUserByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在");
        }
        List<Permission> permissions = this.permissionMapper.findByAdminUserId(user.getId().intValue());
        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        for (Permission permission : permissions) {
            if (permission != null && permission.getName() != null) {
                SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(permission.getName());
                grantedAuthorities.add(simpleGrantedAuthority);
            }
        }
        for (Role role : user.getRoles()) {
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        user.setAuthorities(grantedAuthorities);
        return user;
    }
}
