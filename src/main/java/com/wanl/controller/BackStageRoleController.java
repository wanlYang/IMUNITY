/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.controller;

import com.wanl.controller.BackStageRoleController;
import com.wanl.entity.Result;
import com.wanl.entity.Role;
import com.wanl.service.RoleService;
import com.wanl.service.RoleUserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({ "/admin/manager/role" })
public class BackStageRoleController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleUserService roleUserService;

    @RequestMapping(value = { "/get/list" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result roleList(@RequestParam(value = "flag", required = false) Integer flag) {
        Result result = new Result();
        List<Role> roleList = this.roleService.getRoleList(flag);
        result.setStatus(Integer.valueOf(200));
        result.setMessage("获取成功!");
        result.setData(roleList);
        return result;
    }

    @RequestMapping(value = { "/get/list/table" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result roleListTable() {
        Result result = new Result();
        List<Role> roleList = this.roleService.getRoleList(null);
        result.setStatus(Integer.valueOf(200));
        result.setMessage("获取成功!");
        result.setData(roleList);
        result.setCount(this.roleService.getRoleCount());
        return result;
    }

    @RequestMapping(value = { "/get/usercount" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result roleUserCount(Integer id) {
        Result result = new Result();
        Integer roleUserCount = this.roleUserService.getRoleUserCount(id);
        result.setStatus(Integer.valueOf(200));
        result.setMessage("获取成功!");
        result.setData(roleUserCount);
        return result;
    }

    @RequestMapping(value = { "/submit/add" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result addRole(Role role) {
        Result result = new Result();
        Integer row = this.roleService.addRole(role);
        if (row.intValue() == 0) {
            result.setStatus(Integer.valueOf(-1));
            result.setMessage("本系统最多只能有3个权限!");
            return result;
        }
        result.setStatus(Integer.valueOf(200));
        result.setMessage("添加成功!");
        result.setData(row);
        return result;
    }

    @RequestMapping(value = { "/submit/del" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result delRole(Integer id) {
        Result result = new Result();
        Integer row = this.roleService.delRole(id);
        if (row.intValue() == -1) {
            result.setStatus(Integer.valueOf(-1));
            result.setMessage("暂时无法删除此权限!");
            return result;
        }
        result.setStatus(Integer.valueOf(200));
        result.setMessage("删除成功!");
        result.setData(row);
        return result;
    }
}
