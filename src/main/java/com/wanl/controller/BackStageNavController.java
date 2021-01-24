/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.controller;

import com.wanl.controller.BackStageNavController;
import com.wanl.entity.BackStageNav;
import com.wanl.entity.Permission;
import com.wanl.entity.Result;
import com.wanl.service.BackStageNavService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({ "/admin/manager/nav" })
public class BackStageNavController {
    @Autowired
    private BackStageNavService backStageNavService;

    @RequestMapping(value = { "/get/top" }, method = { RequestMethod.GET })
    @ResponseBody
    public Result name() {
        Result result = new Result();
        List<BackStageNav> adminNavs = this.backStageNavService.getAllTopLevel();
        result.setStatus(Integer.valueOf(200));
        result.setMessage("获取成功!");
        result.setData(adminNavs);
        return result;
    }

    @RequestMapping(value = { "/get/other" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result other(@RequestParam("menuId") Integer menuId) {
        Result result = new Result();
        List<BackStageNav> adminSecondLevel = this.backStageNavService.getSecondLevel(menuId);
        result.setStatus(Integer.valueOf(200));
        result.setMessage("获取成功!");
        result.setData(adminSecondLevel);
        return result;
    }

    @RequestMapping(value = { "/get/nav/list" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result navList(Integer page, Integer limit) {
        Result result = new Result();
        List<BackStageNav> navList = this.backStageNavService.getAllNav(page, limit);
        Integer count = this.backStageNavService.getNavCount();
        result.setStatus(Integer.valueOf(200));
        result.setMessage("获取成功!");
        result.setCount(count);
        result.setData(navList);
        return result;
    }

    @RequestMapping(value = { "/submit/nav" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result addNav(BackStageNav backStageNav, boolean isAutoAddPerm) {
        Integer row = this.backStageNavService.addNav(backStageNav, isAutoAddPerm);
        Result result = new Result();
        if (row.intValue() == -2) {
            result.setStatus(Integer.valueOf(-1));
            result.setMessage("添加失败!最多拥有8个顶级导航栏!");
        } else if (row.intValue() == 1) {
            result.setStatus(Integer.valueOf(200));
            result.setMessage("添加成功!");
        }
        return result;
    }

    @RequestMapping(value = { "/submit/nav/edit" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result editNav(BackStageNav backStageNav) {
        Integer row = this.backStageNavService.editNav(backStageNav);
        Result result = new Result();
        if (row.intValue() != 0) {
            result.setStatus(Integer.valueOf(200));
            result.setMessage("编辑成功!");
        } else {
            result.setStatus(Integer.valueOf(-1));
            result.setMessage("编辑失败!");
        }
        return result;
    }

    @RequestMapping(value = { "/page/nav/top" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result addNavFindTopLevel(@RequestParam(value = "flag", required = false) Integer flag) {
        Result result = new Result();
        List<BackStageNav> adminSecondLevel = this.backStageNavService.getAllTopLevel(flag);
        result.setStatus(Integer.valueOf(200));
        result.setMessage("获取成功!");
        result.setData(adminSecondLevel);
        return result;
    }

    @RequestMapping(value = { "/top/nav/title" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result getTitle(Integer id) {
        Result result = new Result();
        BackStageNav backStageNav = this.backStageNavService.getBackStageNav(id);
        result.setStatus(Integer.valueOf(200));
        result.setMessage("获取成功!");
        result.setData(backStageNav);
        return result;
    }

    @RequestMapping(value = { "/submit/nav/delete" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result delNav(Integer id) {
        Result result = new Result();
        Integer row = this.backStageNavService.delBackStageNav(id);
        if (row.intValue() == -2) {
            result.setStatus(Integer.valueOf(-1));
            result.setMessage("删除失败!包含子导航");
            return result;
        }
        result.setStatus(Integer.valueOf(200));
        result.setMessage("删除成功!");
        result.setData(row);
        return result;
    }

    @RequestMapping(value = { "/get/nav/role/list" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result navRoleList(Integer page, Integer limit) {
        Result result = new Result();
        List<Permission> permissions = this.backStageNavService.getNavRoleList(page, limit);
        result.setStatus(Integer.valueOf(200));
        result.setMessage("删除成功!");
        result.setData(permissions);
        result.setCount(this.backStageNavService.getNavPermissionCount());
        return result;
    }

    @RequestMapping(value = { "/submit/nav/role" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result addNavRole(Permission permission, String rolesString) {
        Result result = new Result();
        boolean isSuccess = this.backStageNavService.addNavPermission(permission, rolesString);
        if (isSuccess) {
            result.setStatus(Integer.valueOf(200));
            result.setMessage("添加成功!当重新登陆时生效");
            result.setData(Boolean.valueOf(isSuccess));
            return result;
        }
        result.setStatus(Integer.valueOf(-1));
        result.setMessage("添加失败!");
        result.setData(Boolean.valueOf(isSuccess));
        return result;
    }

    @RequestMapping(value = { "/submit/nav/role/edit" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result editNavRole(Permission permission, String rolesString) {
        Result result = new Result();
        boolean isSuccess = this.backStageNavService.editNavPermission(permission, rolesString);
        if (isSuccess) {
            result.setStatus(Integer.valueOf(200));
            result.setMessage("编辑成功!当重新登陆时生效");
            result.setData(Boolean.valueOf(isSuccess));
            return result;
        }
        result.setStatus(Integer.valueOf(-1));
        result.setMessage("编辑失败!");
        result.setData(Boolean.valueOf(isSuccess));
        return result;
    }

    @RequestMapping(value = { "/submit/nav/role/delete" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result delNavRole(Integer id) {
        Result result = new Result();
        boolean isSuccess = this.backStageNavService.delNavPermission(id);
        if (isSuccess) {
            result.setStatus(Integer.valueOf(200));
            result.setMessage("删除成功!当重新登陆时生效");
            result.setData(Boolean.valueOf(isSuccess));
            return result;
        }
        result.setStatus(Integer.valueOf(-1));
        result.setMessage("删除失败!");
        result.setData(Boolean.valueOf(isSuccess));
        return result;
    }
}
