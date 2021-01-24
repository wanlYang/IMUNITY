/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.mapper;

import com.wanl.entity.Account;
import com.wanl.entity.EsmAccount;
import com.wanl.entity.User;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AccountMapper {
    List<Account> findAccountList(@Param("start") Integer paramInteger1, @Param("limit") Integer paramInteger2);

    Integer findAccountCount();

    Integer del(@Param("id") Integer paramInteger);

    Integer createAccount(User paramUser);

    Integer changeStatus(@Param("id") Integer paramInteger1, @Param("status") Integer paramInteger2);

    User getUserByAccountId(@Param("id") Integer paramInteger);

    Integer create(EsmAccount paramEsmAccount);

    EsmAccount get(String paramString);

    Integer update(EsmAccount paramEsmAccount);
}

