/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.service.impl;

import com.wanl.annotation.SwitchingDataSource;
import com.wanl.entity.UserOperateLog;
import com.wanl.mapper.UserOperateLogMapper;
import com.wanl.service.UserOperateLogService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SwitchingDataSource
public class UserOperateLogImpl implements UserOperateLogService {
    @Autowired
    UserOperateLogMapper userOperateLogMapper;

    public void handlerLog(UserOperateLog log) {
        this.userOperateLogMapper.createLog(log);
    }

    public List<UserOperateLog> getLogs(Integer page, Integer limit) {
        return this.userOperateLogMapper
                .getUserOperateLogListByLimit(Integer.valueOf((page.intValue() - 1) * limit.intValue()), limit);
    }

    public Integer getLogsCount() {
        return this.userOperateLogMapper.getLogsCount();
    }
}
