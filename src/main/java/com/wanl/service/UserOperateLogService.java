/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.service;

import com.wanl.entity.UserOperateLog;
import java.util.List;

public interface UserOperateLogService {
  void handlerLog(UserOperateLog paramUserOperateLog);
  
  List<UserOperateLog> getLogs(Integer paramInteger1, Integer paramInteger2);
  
  Integer getLogsCount();
}


/* Location:              D:\java\web.war!\WEB-INF\lib\service-1.0-SNAPSHOT.jar!\com\wanl\service\UserOperateLogService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */
