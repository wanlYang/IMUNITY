/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.mapper;

import com.wanl.entity.BackStageNav;
import com.wanl.entity.Permission;
import com.wanl.entity.RolePermission;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BackStageNavMapper {
    List<BackStageNav> getTopLevel(int paramInt);

    List<BackStageNav> getSecondLevel(Integer paramInteger);

    List<BackStageNav> getNav(Integer paramInteger);

    List<BackStageNav> findAllNav(@Param("start") Integer paramInteger1, @Param("limit") Integer paramInteger2);

    List<BackStageNav> findAll();

    Integer findNavCount();

    Integer createNav(BackStageNav paramBackStageNav);

    BackStageNav findNavById(Integer paramInteger);

    Integer delete(Integer paramInteger);

    Integer updateNav(BackStageNav paramBackStageNav);

    List<Permission> findAllNavPermission(@Param("start") Integer paramInteger1, @Param("limit") Integer paramInteger2);

    Integer findNavPermissionCount();

    Integer createNavPermission(Permission paramPermission);

    Integer insertRolePermission(RolePermission paramRolePermission);

    Permission getNavPermission(Integer paramInteger);

    Integer delNavPermission(Integer paramInteger);

    Integer delNavRolePermission(Integer paramInteger);

    Integer updateNavPermission(Permission paramPermission);
}

