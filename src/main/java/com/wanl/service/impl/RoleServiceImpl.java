/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.service.impl;

import com.wanl.annotation.SwitchingDataSource;
import com.wanl.entity.Role;
import com.wanl.mapper.RoleMapper;
import com.wanl.service.RoleService;
import com.wanl.service.RoleUserService;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SwitchingDataSource
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleUserService roleUserService;

    public Role getRoleById(Integer id) {
        Role role = null;
        if (id != null) {
            role = this.roleMapper.getRoleById(id);
        }
        return (role != null) ? role : null;
    }

    public List<Role> getRoleList(Integer flag) {
        List<Role> roleList = this.roleMapper.getRoleList();
        if (flag != null && flag.equals(Integer.valueOf(1))) {
            Iterator<Role> iterator = roleList.iterator();
            while (iterator.hasNext()) {
                Role role = (Role) iterator.next();
                if (role.getName().equals("ROLE_USER")) {
                    iterator.remove();
                }
            }
        }
        return roleList;
    }

    public Integer getRoleCount() {
        return this.roleMapper.findRoleCount();
    }

    public Integer addRole(Role role) {
        Integer row = null;
        if (role != null) {
            Integer findRoleCount = this.roleMapper.findRoleCount();
            int condition = 2;
            if (findRoleCount.intValue() > condition) {
                return Integer.valueOf(0);
            }
            row = this.roleMapper.createRole(role);
        }
        return Integer.valueOf((row != null) ? row.intValue() : 0);
    }

    public Integer delRole(Integer id) {
        Integer row = null;
        if (id != null) {
            Integer roleUserCount = this.roleUserService.getRoleUserCount(id);
            if (roleUserCount.intValue() > 0) {
                return Integer.valueOf(-1);
            }
            row = this.roleMapper.del(id);
        }
        return Integer.valueOf((row != null) ? row.intValue() : 0);
    }
}
