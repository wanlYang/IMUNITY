/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.service;

import com.wanl.entity.Role;
import java.util.List;

public interface RoleService {
  Role getRoleById(Integer paramInteger);
  
  List<Role> getRoleList(Integer paramInteger);
  
  Integer getRoleCount();
  
  Integer addRole(Role paramRole);
  
  Integer delRole(Integer paramInteger);
}


/* Location:              D:\java\web.war!\WEB-INF\lib\service-1.0-SNAPSHOT.jar!\com\wanl\service\RoleService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */
