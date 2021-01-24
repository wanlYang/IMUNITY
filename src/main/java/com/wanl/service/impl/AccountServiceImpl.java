/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.service.impl;

import com.wanl.annotation.SwitchingDataSource;
import com.wanl.entity.Account;
import com.wanl.entity.EsmAccount;
import com.wanl.entity.EsmUser;
import com.wanl.entity.Role;
import com.wanl.entity.User;
import com.wanl.mapper.AccountMapper;
import com.wanl.service.AccountService;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SwitchingDataSource
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountMapper accountMapper;
    //@Autowired
    //private UserMapper userMapper;

    public List<Account> accountList(Integer page, Integer limit) {
        List<Account> list = null;
        if (page != null && limit != null) {
            list = this.accountMapper.findAccountList(Integer.valueOf((page.intValue() - 1) * limit.intValue()), limit);
        }
        return list;
    }

    public Integer getAccountCount() {
        return this.accountMapper.findAccountCount();
    }

    public Integer delAccount(Integer id) {
        Integer row = null;
        if (id != null) {
            row = this.accountMapper.del(id);
        }
        return Integer.valueOf((row != null) ? row.intValue() : 0);
    }

    public Integer addAccount(User user) {
        Integer row = null;
        if (user != null) {
            row = this.accountMapper.createAccount(user);
        }
        return Integer.valueOf((row != null) ? row.intValue() : 0);
    }

    public Integer changeStatus(Integer id, Integer status, String roleSuperAdmin) {
        Integer row = null;
        boolean condition = (id != null && status != null && (status.intValue() == 0 || status.intValue() == 1)
                && roleSuperAdmin != null);
        if (condition) {
            row = this.accountMapper.changeStatus(id, Integer.valueOf((status.intValue() == 0) ? 1 : 0));
        } else {
            User user = this.accountMapper.getUserByAccountId(id);
            if (user != null && user.getRoles() != null) {
                Iterator<?> iterator = user.getRoles().iterator();
                if (iterator.hasNext()) {
                    Role role = (Role) iterator.next();
                    if (role.getName().equals("ROLE_ADMIN") || role.getName().equals("ROLE_SUPER_ADMIN")) {
                        return Integer.valueOf(-1);
                    }
                }

            }

            row = this.accountMapper.changeStatus(id, Integer.valueOf((status.intValue() == 0) ? 1 : 0));
        }
        if (row.intValue() != 0) {
            return Integer.valueOf((status.intValue() == 0) ? 1 : 0);
        }
        return Integer.valueOf(0);
    }

    public User getUserByAccountId(Integer id) {
        User user = null;
        if (id != null) {
            user = this.accountMapper.getUserByAccountId(id);
        }
        return user;
    }

    public Integer create(EsmAccount account) {
        boolean condition = (account != null);
        if (!condition) {
            return Integer.valueOf(0);
        }
        return this.accountMapper.create(account);
    }

    public EsmAccount get(String id) {
        return this.accountMapper.get(id);
    }

    public Integer recharge(EsmUser user, Double money) {
        EsmAccount account = this.accountMapper.get(user.getId());
        account.setBalance(Double.valueOf(account.getBalance().doubleValue() + money.doubleValue()));
        return this.accountMapper.update(account);
    }
}
