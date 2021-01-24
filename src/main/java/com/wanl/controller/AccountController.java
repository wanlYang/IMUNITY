/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.controller;

import com.wanl.constant.ImunityConstant;
import com.wanl.controller.AccountController;
import com.wanl.entity.EsmAccount;
import com.wanl.entity.EsmUser;
import com.wanl.entity.Result;
import com.wanl.service.AccountService;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({ "/account" })
public class AccountController {
    @Autowired
    private AccountService accountService;

    @RequestMapping(value = { "/recharge" }, method = { RequestMethod.GET })
    public ModelAndView rechargePage(ModelAndView modelAndView, String backUrl, HttpSession session) {
        EsmUser user = (EsmUser) session.getAttribute(ImunityConstant.USER_SESSION);
        EsmAccount account = this.accountService.get(user.getId());
        modelAndView.addObject("backUrl", backUrl);
        modelAndView.setViewName("templates/account_recharge");
        modelAndView.addObject("account", account);
        return modelAndView;
    }

    @RequestMapping(value = { "/recharge/submit" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result recharge(Double money, HttpSession session) {
        EsmUser user = (EsmUser) session.getAttribute(ImunityConstant.USER_SESSION);
        if (user == null) {
            return new Result(Integer.valueOf(-1), ImunityConstant.ABNORMAL, Integer.valueOf(0), Integer.valueOf(0));
        }
        Integer row = this.accountService.recharge(user, money);
        EsmAccount account = this.accountService.get(user.getId());
        return new Result(Integer.valueOf(200), "充值成功!", row, account);
    }
}
