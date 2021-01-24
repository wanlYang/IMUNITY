/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.controller;

import com.wanl.controller.BackStageThemeController;
import com.wanl.entity.CommunityTheme;
import com.wanl.entity.Result;
import com.wanl.service.ThemeService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({ "/admin/manager/theme" })
public class BackStageThemeController {
    @Autowired
    private ThemeService themeService;

    @RequestMapping(value = { "/get/list" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result getThemeList() {
        Result result = new Result();
        List<CommunityTheme> communityThemes = this.themeService.getThemeList();
        result.setStatus(Integer.valueOf(200));
        result.setMessage("获取成功!");
        result.setData(communityThemes);
        return result;
    }

    @RequestMapping(value = { "/get/theme/count" }, method = { RequestMethod.GET })
    @ResponseBody
    public Result getThemeCount() {
        Result result = new Result();
        Integer count = this.themeService.getCount();
        if (count != null) {
            result.setStatus(Integer.valueOf(200));
            result.setMessage("获取成功!");
            result.setCount(count);
            return result;
        }
        result.setStatus(Integer.valueOf(-1));
        result.setMessage("获取失败!");
        result.setCount(null);
        return result;
    }
}
