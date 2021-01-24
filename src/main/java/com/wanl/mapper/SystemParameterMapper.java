/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.mapper;

import com.wanl.entity.SystemParameter;
import java.util.List;

public interface SystemParameterMapper {
    Integer createSystemParameter(SystemParameter paramSystemParameter);

    List<SystemParameter> getSystemParameter();

    Integer updateSystemParameter(SystemParameter paramSystemParameter);
}

