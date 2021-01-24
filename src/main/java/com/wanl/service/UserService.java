/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.service;

import com.wanl.entity.OpenUser;
import com.wanl.entity.User;
import com.wanl.entity.UserQueryParam;
import java.util.List;

public interface UserService {
  Integer addUser(User paramUser, String paramString);
  
  User getUserByUserName(String paramString);
  
  List<User> getAllUserList();
  
  List<User> getAllUserList(Integer paramInteger1, Integer paramInteger2);
  
  Integer getUserCount();
  
  Integer usableUser(Integer paramInteger1, Integer paramInteger2);
  
  Integer deleteUser(Integer paramInteger, String paramString);
  
  User editUser(User paramUser, String paramString);
  
  Integer deleteUser(String[] paramArrayOfString, String paramString);
  
  Integer CheckUsername(String paramString);
  
  Integer CheckEmail(String paramString);
  
  User editUserRole(Integer paramInteger, String paramString);
  
  List<User> getAllUserList(Integer paramInteger1, Integer paramInteger2, UserQueryParam paramUserQueryParam);
  
  Integer getUserCount(UserQueryParam paramUserQueryParam);
  
  User getUser(Integer paramInteger);
  
  User updateAdmin(User paramUser);
  
  Integer updateAdminHeadImg(String paramString, Integer paramInteger);
  
  User getUserByOpenId(String paramString1, String paramString2);
  
  Integer authorizationOpenInfo(OpenUser paramOpenUser);
  
  boolean checkAuthorizationOpenInfo(String paramString1, String paramString2);
  
  boolean cancelOpenOauth(Integer paramInteger, String paramString);
  
  OpenUser getOpenUserByOpenIdAndUserId(String paramString1, Integer paramInteger, String paramString2);
  
  OpenUser getOpenUserByUserId(Integer paramInteger, String paramString);
  
  Integer updateOpenUser(OpenUser paramOpenUser);
  
  OpenUser getOpenUserByOpenId(String paramString1, String paramString2);
  
  boolean checkOldPassword(User paramUser, String paramString);
  
  boolean changePassword(String paramString, User paramUser);
}


/* Location:              D:\java\web.war!\WEB-INF\lib\service-1.0-SNAPSHOT.jar!\com\wanl\service\UserService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */
