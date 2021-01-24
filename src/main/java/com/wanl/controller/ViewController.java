/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ViewController {
    @RequestMapping(value = { "/login" }, method = { RequestMethod.GET })
    public String login() {
        return "login";
    }

    @RequestMapping(value = { "/regist" }, method = { RequestMethod.GET })
    public String regist() {
        return "regist";
    }
}
