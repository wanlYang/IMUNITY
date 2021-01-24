/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.mapper;

import com.wanl.entity.Permission;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@ResponseBody
public interface PermissionMapper {
    List<Permission> findAll();

    List<Permission> findByAdminUserId(int paramInt);
}

