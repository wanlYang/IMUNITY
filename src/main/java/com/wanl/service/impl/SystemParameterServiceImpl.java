/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.service.impl;

import com.wanl.annotation.SwitchingDataSource;
import com.wanl.entity.SystemParameter;
import com.wanl.mapper.SystemParameterMapper;
import com.wanl.service.SystemParameterService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SwitchingDataSource
public class SystemParameterServiceImpl implements SystemParameterService {
    @Autowired
    private SystemParameterMapper systemParameterMapper;

    public Integer changeSystemParameter(SystemParameter systemParameter) {
        if (systemParameter != null) {
            return this.systemParameterMapper.updateSystemParameter(systemParameter);
        }
        return null;
    }

    public List<SystemParameter> getSystemParameter() {
        return this.systemParameterMapper.getSystemParameter();
    }
}
