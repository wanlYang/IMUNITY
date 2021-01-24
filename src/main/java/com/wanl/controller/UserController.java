/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.controller;

import com.wanl.constant.EsmConstant;
import com.wanl.constant.ImunityConstant;
import com.wanl.controller.UserController;
import com.wanl.entity.EsmUser;
import com.wanl.entity.Result;
import com.wanl.redis.RedisCacheManager;
import com.wanl.service.EsmUserService;
import com.wanl.service.ShopCartService;
import com.wanl.utils.CookieUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping({ "/user" })
@Controller
public class UserController {
    @Autowired
    private EsmUserService userService;
    @Autowired
    private RedisCacheManager redisCacheManager;
    @Autowired
    private ShopCartService shopCartService;

    @RequestMapping(value = { "/regist/submit" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result regist(EsmUser user, String phoneCode, String confirmPassword, HttpServletRequest request,
            HttpServletResponse response) {
        Result registResult = this.userService.regist(user, phoneCode, confirmPassword);
        if (registResult.getStatus().intValue() == 200) {
            String cookie = CookieUtil.getCookie(request, EsmConstant.SMS_CODE, false);
            String tempPhone = (String) this.redisCacheManager.get(EsmConstant.TEMP_PHONE);
            if (tempPhone != null) {
                this.redisCacheManager.del(new String[] { EsmConstant.TEMP_PHONE });
            }
            if (cookie != null) {
                this.redisCacheManager.del(new String[] { cookie });
                CookieUtil.delCookie(response, request, EsmConstant.SMS_CODE);
            }
        }
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return registResult;
    }

    @RequestMapping(value = { "/login/submit" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result login(String username, String password, HttpSession session) {
        Result loginResult = this.userService.login(username, password);
        if (loginResult.getStatus().intValue() == 200) {
            session.setAttribute(ImunityConstant.USER_SESSION, loginResult.getData());
            EsmUser user = (EsmUser) session.getAttribute(ImunityConstant.USER_SESSION);
            if (user != null) {
                Result shopCartPiece = this.shopCartService.getShopCartPiece(user.getId());
                session.setAttribute("SHOP_CART_PIECE", shopCartPiece.getData());
            }
        }
        session.setMaxInactiveInterval(600);
        return loginResult;
    }

    @RequestMapping(value = { "/logout" }, method = { RequestMethod.GET })
    public String logout(HttpSession session) {
        session.removeAttribute(ImunityConstant.USER_SESSION);
        session.invalidate();
        return "redirect:/";
    }
}
