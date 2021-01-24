/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.service.impl;

import com.wanl.annotation.SwitchingDataSource;
import com.wanl.mapper.RoleUserMapper;
import com.wanl.service.RoleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SwitchingDataSource
public class RoleUserServiceImpl implements RoleUserService {
    @Autowired
    private RoleUserMapper roleUserMapper;

    public Integer getRoleUserCount(Integer id) {
        Integer roleUserCount = Integer.valueOf(0);
        if (id != null) {
            roleUserCount = this.roleUserMapper.getRoleUserCount(id);
        }
        return Integer.valueOf((roleUserCount != null) ? roleUserCount.intValue() : 0);
    }
}
