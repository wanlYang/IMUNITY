/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.controller;

import com.wanl.constant.EsmConstant;
import com.wanl.controller.MessageController;
import com.wanl.redis.RedisCacheManager;
import com.wanl.utils.CookieUtil;
import com.wanl.utils.RegexUtils;
import com.wanl.utils.SendSms;
import com.wanl.utils.UUIDUtils;
import com.wanl.utils.UtilsResult;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({ "/message" })
public class MessageController {
    @Autowired
    private RedisCacheManager redisCacheManager;

    @RequestMapping(value = { "/send/phone/code" }, method = { RequestMethod.POST })
    @ResponseBody
    public UtilsResult sendPhoneCode(String phone, HttpServletResponse response, HttpServletRequest request) {
        UtilsResult result = new UtilsResult();
        if (!RegexUtils.checkMobile(phone)) {
            result.setMessage("手机号有误!");
            result.setStatus(Integer.valueOf(-1));
            return result;
        }
        String cookie = CookieUtil.getCookie(request, EsmConstant.SMS_CODE, false);
        if (cookie != null) {
            this.redisCacheManager.del(new String[] { cookie });
            CookieUtil.delCookie(response, request, EsmConstant.SMS_CODE);
        }
        String smsCode = SendSms.getSMSCode();
        UtilsResult responseStatus = SendSms.sendMessageCode(smsCode, phone);
        System.out.println(((String) responseStatus.getData()));
        if (((String) responseStatus.getData()).equals(EsmConstant.SEND_CODE_SUCCESS)) {
            String codeKey = UUIDUtils.getCode();
            this.redisCacheManager.set(codeKey, smsCode, 300);
            this.redisCacheManager.set(EsmConstant.TEMP_PHONE, phone, 300);
            CookieUtil.addCookie(response, EsmConstant.SMS_CODE, codeKey, Integer.valueOf(300), false);
        }
        return responseStatus;
    }
}
