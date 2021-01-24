/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.service;

import com.wanl.entity.CommunityTheme;
import java.util.List;

public interface ThemeService {
  List<CommunityTheme> getThemeList();
  
  CommunityTheme getTheme(Integer paramInteger);
  
  Integer getCount();
}


/* Location:              D:\java\web.war!\WEB-INF\lib\service-1.0-SNAPSHOT.jar!\com\wanl\service\ThemeService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */
