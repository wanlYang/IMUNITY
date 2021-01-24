/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.controller;

import com.wanl.constant.ImunityConstant;
import com.wanl.entity.Account;
import com.wanl.entity.Result;
import com.wanl.entity.Role;
import com.wanl.entity.User;
import com.wanl.entity.UserQueryParam;
import com.wanl.service.AccountService;
import com.wanl.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({ "/admin/manager/user" })
public class BackStageUserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AccountService accountService;

    @RequestMapping(value = { "/get/count" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result getUserCount(String data) {
        Result result = new Result();
        result.setStatus(Integer.valueOf(200));
        result.setData(data);
        result.setMessage("获取成功!");
        result.setCount(this.userService.getUserCount());
        return result;
    }

    @RequestMapping(value = { "/get/list" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result getUserList(Integer page, Integer limit, UserQueryParam userQueryParam) {
        Result result = new Result();
        List<User> allUserList = this.userService.getAllUserList(page, limit, userQueryParam);
        result.setData(allUserList);
        result.setStatus(Integer.valueOf(200));
        result.setMessage("获取成功!");
        result.setCount(this.userService.getUserCount(userQueryParam));
        return result;
    }

    @RequestMapping(value = { "/usable" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result usable(Integer id, Integer status) {
        Result result = new Result();
        Integer backStatus = this.userService.usableUser(id, status);
        result.setData(backStatus);
        result.setStatus(Integer.valueOf(200));
        return result;
    }

    @RequestMapping(value = { "/submit/add" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result add(User user, String stringRoles) {
        Result result = new Result();
        Integer row = this.userService.addUser(user, stringRoles);
        int con = -2;
        if (row.intValue() == con) {
            result.setMessage("添加失败!账号或邮箱重复!");
            result.setData(row);
            result.setStatus(Integer.valueOf(-1));
            return result;
        }
        result.setMessage("添加成功!");
        result.setData(row);
        result.setStatus(Integer.valueOf(200));
        return result;
    }

    @RequestMapping(value = { "/submit/delete" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result delete(Integer id, Authentication authentication) {
        Result result = new Result();
        User admin = (User) authentication.getPrincipal();
        if (id.equals(admin.getId())) {
            result.setMessage("删除失败!");
            return result;
        }
        Integer row = Integer.valueOf(0);
        for (Role role : admin.getRoles()) {
            if (role.getName().equals(ImunityConstant.ROLE_SUPER_ADMIN)) {
                row = this.userService.deleteUser(id, ImunityConstant.ROLE_SUPER_ADMIN);
                break;
            }
            if (role.getName().equals(ImunityConstant.ROLE_ADMIN)) {
                row = this.userService.deleteUser(id, null);
                break;
            }
        }
        if (row.intValue() == 0) {
            result.setMessage("删除失败!");
            result.setData(row);
            result.setStatus(Integer.valueOf(-1));
            return result;
        }
        result.setMessage("删除成功!");
        result.setData(row);
        result.setStatus(Integer.valueOf(200));
        return result;
    }

    @RequestMapping(value = { "/submit/batch/delete" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result batchDelete(@RequestBody String[] delId, Authentication authentication) {
        Result result = new Result();
        User admin = (User) authentication.getPrincipal();
        Integer row = Integer.valueOf(0);
        String[] arrayOfString = delId;
        for (String id :arrayOfString) {
            if (Integer.parseInt(id) == admin.getId().intValue()) {
                result.setMessage("删除失败!");
                return result;
            }
        }

        for (Role role : admin.getRoles()) {
            if (role.getName().equals(ImunityConstant.ROLE_SUPER_ADMIN)) {
                row = this.userService.deleteUser(delId, ImunityConstant.ROLE_SUPER_ADMIN);
                break;
            }
            if (role.getName().equals(ImunityConstant.ROLE_ADMIN)) {
                row = this.userService.deleteUser(delId, null);
                break;
            }
        }
        result.setData(row);
        result.setMessage("删除成功(不包含管理员)!");
        result.setStatus(Integer.valueOf(200));
        return result;
    }

    @RequestMapping(value = { "/submit/edit" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result edit(User user, String stringRoles) {
        Result result = new Result();
        User backUser = this.userService.editUser(user, stringRoles);
        result.setMessage("修改成功!");
        result.setData(backUser);
        result.setStatus(Integer.valueOf(200));
        return result;
    }

    @RequestMapping(value = { "/submit/role" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result edit(Integer id, String stringRoles) {
        Result result = new Result();
        User backUser = this.userService.editUserRole(id, stringRoles);
        result.setMessage("修改成功!");
        result.setData(backUser);
        result.setStatus(Integer.valueOf(200));
        return result;
    }

    @RequestMapping(value = { "/get/account/list" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result getAccount(Integer page, Integer limit) {
        Result result = new Result();
        List<Account> accountList = this.accountService.accountList(page, limit);
        result.setMessage("获取成功!");
        result.setData(accountList);
        result.setStatus(Integer.valueOf(200));
        result.setCount(this.accountService.getAccountCount());
        return result;
    }

    @RequestMapping(value = { "/account/change/status" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result delAccount(Integer id, Integer status, Authentication authentication) {
        Result result = new Result();
        User admin = (User) authentication.getPrincipal();
        Integer backStatus = Integer.valueOf(0);
        for (Role role : admin.getRoles()) {
            if (role.getName().equals(ImunityConstant.ROLE_SUPER_ADMIN)) {
                backStatus = this.accountService.changeStatus(id, status, ImunityConstant.ROLE_SUPER_ADMIN);
                break;
            }
            if (role.getName().equals(ImunityConstant.ROLE_ADMIN)) {
                backStatus = this.accountService.changeStatus(id, status, null);
                break;
            }
        }
        if (backStatus.intValue() == -1) {
            result.setMessage("修改失败!管理员!");
            result.setData(backStatus);
            result.setStatus(Integer.valueOf(-1));
            return result;
        }
        result.setMessage("修改成功!");
        result.setData(backStatus);
        result.setStatus(Integer.valueOf(200));
        return result;
    }
}
