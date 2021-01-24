/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.service;

import com.wanl.entity.EsmUser;
import com.wanl.entity.Result;

public interface EsmUserService {
  Result regist(EsmUser EsmUser, String phoneCode, String confirmPassword);
  
  Result login(String username, String password);
}
