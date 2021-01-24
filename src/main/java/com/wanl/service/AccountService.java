/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.service;

import com.wanl.entity.Account;
import com.wanl.entity.EsmAccount;
import com.wanl.entity.EsmUser;
import com.wanl.entity.User;
import java.util.List;

public interface AccountService {
  List<Account> accountList(Integer paramInteger1, Integer paramInteger2);
  
  Integer getAccountCount();
  
  Integer delAccount(Integer paramInteger);
  
  Integer addAccount(User paramUser);
  
  Integer changeStatus(Integer paramInteger1, Integer paramInteger2, String paramString);
  
  User getUserByAccountId(Integer paramInteger);
  
  Integer create(EsmAccount paramEsmAccount);
  
  EsmAccount get(String paramString);
  
  Integer recharge(EsmUser paramEsmUser, Double paramDouble);
}


/* Location:              D:\java\web.war!\WEB-INF\lib\service-1.0-SNAPSHOT.jar!\com\wanl\service\AccountService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.0.7
 */
