/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.mapper;

import com.wanl.entity.RoleUser;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RoleUserMapper {
    Integer insertRoleUser(RoleUser paramRoleUser);

    Integer deleteRoleUserById(@Param("id") Integer paramInteger);

    Integer deleteRoleUserByUserId(@Param("id") Integer paramInteger);

    List<RoleUser> getRoleUserByUserId(@Param("id") Integer paramInteger);

    RoleUser getRoleUserByUserIdRoleId(@Param("userId") Integer paramInteger1, @Param("roleId") Integer paramInteger2);

    Integer getRoleUserCount(@Param("id") Integer paramInteger);
}

