/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.mapper;

import com.wanl.entity.UserOperateLog;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserOperateLogMapper {
    void createLog(UserOperateLog paramUserOperateLog);

    List<UserOperateLog> getUserOperateLogList();

    List<UserOperateLog> getUserOperateLogListByLimit(@Param("start") Integer paramInteger1,
            @Param("limit") Integer paramInteger2);

    Integer getLogsCount();
}

