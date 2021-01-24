/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.service.impl;

import com.wanl.annotation.SwitchingDataSource;
import com.wanl.config.MerchantConfig;
import com.wanl.constant.EsmConstant;
import com.wanl.entity.EsmAccount;
import com.wanl.entity.EsmUser;
import com.wanl.entity.Result;
import com.wanl.mapper.EsmUserMapper;
import com.wanl.redis.RedisCacheManager;
import com.wanl.service.AccountService;
import com.wanl.service.EsmUserService;
import com.wanl.utils.Base64Util;
import com.wanl.utils.CookieUtil;
import com.wanl.utils.MD5Util;
import com.wanl.utils.UUIDUtils;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@SwitchingDataSource
public class EsmUserServiceImpl implements EsmUserService {
    @Autowired
    private EsmUserMapper userMapper;
    @Autowired
    private AccountService accountService;
    @Autowired
    private RedisCacheManager redisCacheManager;

    @Autowired
    private MerchantConfig merchantConfig;

    @Transactional(rollbackFor = { Exception.class })
    public Result regist(EsmUser user, String phoneCode, String confirmPassword) {
        boolean condition = (user != null && StringUtils.isNotBlank(phoneCode)
                && StringUtils.isNotBlank(confirmPassword) && StringUtils.isNotBlank(user.getUsername())
                && StringUtils.isNotBlank(user.getPassword()) && StringUtils.isNotBlank(user.getPhone()));
        if (!condition) {
            return new Result(Integer.valueOf(-1011), "参数为空!", Integer.valueOf(0), null);
        }
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        String cookie = CookieUtil.getCookie(request, EsmConstant.SMS_CODE, false);
        if (cookie == null) {
            return new Result(Integer.valueOf(-2001), "验证码无效!", Integer.valueOf(0), null);
        }
        String code = (String) this.redisCacheManager.get(cookie);
        if (code == null) {
            return new Result(Integer.valueOf(-3001), "验证码无效!", Integer.valueOf(0), null);
        }
        if (!phoneCode.equals(code)) {
            return new Result(Integer.valueOf(-4011), "验证码输入错误!", Integer.valueOf(0), null);
        }
        if (!confirmPassword.equals(user.getPassword())) {
            return new Result(Integer.valueOf(-4056), "两次密码不一致!", Integer.valueOf(0), null);
        }
        String tempPhone = (String) this.redisCacheManager.get("TEMP_PHONE");
        if (tempPhone == null) {
            return new Result(Integer.valueOf(-5210), "请刷新重试!", Integer.valueOf(0), null);
        }
        if (!tempPhone.equals(user.getPhone())) {
            return new Result(Integer.valueOf(-6031), "手机号异常!", Integer.valueOf(0), null);
        }
        EsmUser userByUsername = this.userMapper.findUserByUsername(user.getUsername());
        if (userByUsername != null) {
            return new Result(Integer.valueOf(-7652), "用户名已被使用!", Integer.valueOf(0), null);
        }
        EsmUser userByPhone = this.userMapper.findUserByPhone(user.getPhone());
        if (userByPhone != null) {
            return new Result(Integer.valueOf(-7653), "手机已被注册!", Integer.valueOf(0), null);
        }
        String decoder = Base64Util.decoder(MD5Util.MD5Encode(user.getPassword()));
        user.setPassword(decoder);
        user.setId(UUIDUtils.generateNumberUUID("USER_ID"));
        user.setHeadImg(merchantConfig.getDEFAUTLHEADIMG());
        user.setAge(Integer.valueOf(0));
        EsmAccount account = new EsmAccount();
        account.setId(UUIDUtils.generateNumberUUID("ACCOUNT_ID"));
        account.setUser(user);
        account.setBalance(Double.valueOf(0.0D));
        Integer insertUserIsOk = this.userMapper.create(user);
        Integer insertAccountIsOk = this.accountService.create(account);
        if (insertUserIsOk.intValue() == 0 || insertAccountIsOk.intValue() == 0) {
            return new Result(Integer.valueOf(-200), "注册失败!", Integer.valueOf(0), null);
        }
        return new Result(Integer.valueOf(200), "注册成功!", Integer.valueOf(0), null);
    }

    public Result login(String username, String password) {
        if (!StringUtils.isNotBlank(username) && !StringUtils.isNotBlank(password)) {
            return new Result(Integer.valueOf(-1011), "参数为空!");
        }
        EsmUser userByUsername = this.userMapper.findUserByUsername(username);
        if (userByUsername == null) {
            return new Result(Integer.valueOf(-1012), "还用户名尚未注册!");
        }
        String decoder = Base64Util.decoder(MD5Util.MD5Encode(password));
        EsmUser userByNamePass = this.userMapper.findUserByNamePass(username, decoder);
        if (userByNamePass == null) {
            return new Result(Integer.valueOf(-1013), "密码错误!");
        }
        return new Result(Integer.valueOf(200), "登陆成功!", Integer.valueOf(0), userByNamePass);
    }
}
