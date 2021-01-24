/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.service;

import com.wanl.entity.BackStageNav;
import com.wanl.entity.Permission;
import java.util.List;

public interface BackStageNavService {
  List<BackStageNav> getAllTopLevel();
  
  List<BackStageNav> getSecondLevel(Integer paramInteger);
  
  List<BackStageNav> getNav(Integer paramInteger);
  
  List<BackStageNav> getAllNav(Integer paramInteger1, Integer paramInteger2);
  
  List<BackStageNav> getAllNav();
  
  Integer getNavCount();
  
  List<BackStageNav> getAllTopLevel(Integer paramInteger);
  
  Integer addNav(BackStageNav paramBackStageNav, boolean paramBoolean);
  
  BackStageNav getBackStageNav(Integer paramInteger);
  
  Integer delBackStageNav(Integer paramInteger);
  
  List<BackStageNav> treeBackStageNavs(List<BackStageNav> paramList, Integer paramInteger, boolean paramBoolean);
  
  Integer editNav(BackStageNav paramBackStageNav);
  
  List<Permission> getNavRoleList(Integer paramInteger1, Integer paramInteger2);
  
  Integer getNavPermissionCount();
  
  boolean addNavPermission(Permission paramPermission, String paramString);
  
  boolean delNavPermission(Integer paramInteger);
  
  boolean editNavPermission(Permission paramPermission, String paramString);
}


/* Location:              D:\java\web.war!\WEB-INF\lib\service-1.0-SNAPSHOT.jar!\com\wanl\service\BackStageNavService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */
