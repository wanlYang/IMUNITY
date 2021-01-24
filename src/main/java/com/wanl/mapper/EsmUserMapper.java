/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.mapper;

import com.wanl.entity.EsmUser;
import org.apache.ibatis.annotations.Param;

public interface EsmUserMapper {
    Integer create(EsmUser paramEsmUser);

    EsmUser findUserByUsername(String paramString);

    EsmUser findUserByPhone(String paramString);

    EsmUser findUserById(String paramString);

    EsmUser findUserByNamePass(@Param("username") String paramString1, @Param("password") String paramString2);
}

