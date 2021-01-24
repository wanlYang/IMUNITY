/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.mapper;

import com.wanl.entity.Role;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RoleMapper {
    Role getRoleById(@Param("id") Integer paramInteger);

    List<Role> getRoleList();

    Integer findRoleCount();

    Integer createRole(Role paramRole);

    Integer del(@Param("id") Integer paramInteger);
}

