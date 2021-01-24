/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.controller;

import com.wanl.controller.BackStageCheckController;
import com.wanl.entity.Result;
import com.wanl.entity.User;
import com.wanl.service.UserService;
import com.wanl.utils.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({ "/admin/manager/check" })
public class BackStageCheckController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = { "/user/username" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result checkUsername(String username) {
        Result result = new Result();
        Integer count = this.userService.CheckUsername(username);
        if (count.intValue() != 0) {
            result.setMessage("用户名已注册!请重新输入用户名!");
            result.setStatus(Integer.valueOf(-1));
            return result;
        }
        result.setMessage("用户名可用!");
        result.setStatus(Integer.valueOf(200));
        return result;
    }

    @RequestMapping(value = { "/user/email" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result checkUserEmail(String email, @RequestParam(value = "isEdit", required = false) boolean isEdit) {
        Result result = new Result();
        Integer count = this.userService.CheckEmail(email);
        if (count.intValue() != 0) {
            result.setMessage("邮箱已注册!请重新输入邮箱!");
            result.setStatus(Integer.valueOf(-1));
            if (isEdit) {
                if (SpringSecurityUtil.getSecurityAuthentication().getPrincipal() instanceof User) {
                    User principal = (User) SpringSecurityUtil.getSecurityAuthentication().getPrincipal();
                    User user = this.userService.getUser(principal.getId());
                    if (user.getBindemail().equals(email)) {
                        result.setMessage("不改变邮箱!");
                        result.setStatus(Integer.valueOf(200));
                        return result;
                    }
                }
                return result;
            }
            return result;
        }
        result.setMessage("邮箱可用!");
        result.setStatus(Integer.valueOf(200));
        return result;
    }
}
