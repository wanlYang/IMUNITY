/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.service;

import com.wanl.entity.SystemParameter;
import java.util.List;

public interface SystemParameterService {
  List<SystemParameter> getSystemParameter();
  
  Integer changeSystemParameter(SystemParameter paramSystemParameter);
}


/* Location:              D:\java\web.war!\WEB-INF\lib\service-1.0-SNAPSHOT.jar!\com\wanl\service\SystemParameterService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */
