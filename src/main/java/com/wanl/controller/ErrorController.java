/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorController {
    @RequestMapping({ "/401" })
    public ModelAndView unauthorizedHandler(@RequestParam(value = "authorized", required = false) String authorized,
            @RequestParam(value = "qqauthorized", required = false) String qqauthorized, ModelAndView modelAndView) {
        if (authorized != null) {
            modelAndView.addObject("message", "已认证过!");
        }
        if (qqauthorized != null) {
            modelAndView.addObject("message", "QQ已认证过!");
        }
        modelAndView.setViewName("admin/templates/401");
        return modelAndView;
    }
}
