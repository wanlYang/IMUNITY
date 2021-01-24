/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.controller;

import com.wanl.constant.ImunityConstant;
import com.wanl.controller.ManagementController;
import com.wanl.entity.Result;
import com.wanl.entity.SystemParameter;
import com.wanl.entity.User;
import com.wanl.entity.UserOperateLog;
import com.wanl.service.SystemParameterService;
import com.wanl.service.UserOperateLogService;
import com.wanl.service.UserService;
import com.wanl.utils.CookieUtil;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({ "/admin/manager" })
public class ManagementController {
    @Autowired
    private UserOperateLogService userOperateLogService;
    @Autowired
    private SystemParameterService systemParameterService;
    @Autowired
    private UserService userService;

    @RequestMapping({ "/index" })
    public ModelAndView index(ModelAndView modelAndView, HttpServletRequest request, Authentication authentication,
            HttpServletResponse response) {
        if (authentication.isAuthenticated() && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            boolean isAuthQQ = this.userService.checkAuthorizationOpenInfo(user.getId().toString(), "QQ");
            if (isAuthQQ) {
                CookieUtil.addCookie(response, ImunityConstant.QQ_OAUTH2_SUCCESS, ImunityConstant.SUCCESS, Integer.valueOf(604800), true);
                modelAndView.addObject("isAuthQQ", Boolean.valueOf(true));
            } else {
                CookieUtil.delCookie(response, request, ImunityConstant.QQ_OAUTH2_SUCCESS);
                modelAndView.addObject("isAuthQQ", Boolean.valueOf(false));
            }
        }
        modelAndView.setViewName("admin/templates/index");
        return modelAndView;
    }

    @RequestMapping({ "/login/exhibition" })
    public ModelAndView exhibition(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/templates/login/login_exhibition");
        return modelAndView;
    }

    @RequestMapping({ "/main" })
    public ModelAndView main(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/templates/main");
        return modelAndView;
    }

    @RequestMapping({ "/article/list" })
    public ModelAndView articleList(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/templates/article/article_list");
        return modelAndView;
    }

    @RequestMapping({ "/article/add" })
    public ModelAndView addArticle(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/templates/article/add_article");
        return modelAndView;
    }

    @RequestMapping({ "/article/edit" })
    public ModelAndView editArticle(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/templates/article/edit_article");
        return modelAndView;
    }

    @RequestMapping({ "/user/list" })
    public ModelAndView userList(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/templates/user/user_list");
        return modelAndView;
    }

    @RequestMapping({ "/user/add" })
    public ModelAndView addUser(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/templates/user/add_user");
        return modelAndView;
    }

    @RequestMapping({ "/user/showinfo" })
    public ModelAndView editUser(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/templates/user/user_info");
        return modelAndView;
    }

    @RequestMapping({ "/user/edit/role" })
    public ModelAndView editUserRole(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/templates/user/edit_user_role");
        return modelAndView;
    }

    @RequestMapping({ "/user/role" })
    public ModelAndView userGrade(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/templates/user/user_role");
        return modelAndView;
    }

    @RequestMapping({ "/user/role/add" })
    public ModelAndView userRoleAdd(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/templates/user/add_role");
        return modelAndView;
    }

    @RequestMapping({ "/user/account/add" })
    public ModelAndView addUserAccount(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/templates/user/add_account");
        return modelAndView;
    }

    @RequestMapping({ "/user/account/list" })
    public ModelAndView userAccountList(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/templates/user/user_account_list");
        return modelAndView;
    }

    @RequestMapping({ "/system_setting/basic_parameter" })
    public ModelAndView systemBasicParameter(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/templates/system_setting/basic_parameter");
        return modelAndView;
    }

    @RequestMapping({ "/system_setting/logs" })
    public ModelAndView systemLogs(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/templates/system_setting/logs");
        return modelAndView;
    }

    @RequestMapping({ "/system_setting/link_list" })
    public ModelAndView systemLinks(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/templates/system_setting/link_list");
        return modelAndView;
    }

    @RequestMapping({ "/nav/list" })
    public ModelAndView navList(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/templates/system_setting/backstage_nav");
        return modelAndView;
    }

    @RequestMapping({ "/nav/add/page" })
    public ModelAndView addNav(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/templates/system_setting/add_backstage_nav");
        return modelAndView;
    }

    @RequestMapping({ "/nav/edit/page" })
    public ModelAndView editNav(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/templates/system_setting/edit_backstage_nav");
        return modelAndView;
    }

    @RequestMapping({ "/nav/role/list" })
    public ModelAndView navRoleList(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/templates/system_setting/backstage_nav_role");
        return modelAndView;
    }

    @RequestMapping({ "/nav/role/add/page" })
    public ModelAndView navRoleAddPage(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/templates/system_setting/add_backstage_nav_role");
        return modelAndView;
    }

    @RequestMapping({ "/nav/role/edit/page" })
    public ModelAndView editNavRole(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/templates/system_setting/edit_backstage_nav_role");
        return modelAndView;
    }

    @RequestMapping({ "/system_setting/link/add" })
    public ModelAndView addLink(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/templates/system_setting/links_add");
        return modelAndView;
    }

    @RequestMapping({ "/system_setting/link/edit" })
    public ModelAndView editLink(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/templates/system_setting/links_edit");
        return modelAndView;
    }

    @RequestMapping({ "/product/category/list" })
    public ModelAndView category(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/templates/shop/product/category_list");
        return modelAndView;
    }

    @RequestMapping({ "/product/list" })
    public ModelAndView product(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/templates/shop/product/product_list");
        return modelAndView;
    }

    @RequestMapping({ "/shop/category/add/page" })
    public ModelAndView addCategoryPage(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/templates/shop/product/add_category");
        return modelAndView;
    }

    @RequestMapping({ "/shop/product/add/page" })
    public ModelAndView addProductPage(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/templates/shop/product/add_product");
        return modelAndView;
    }

    @RequestMapping({ "/shop/product/edit/page" })
    public ModelAndView editProductPage(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/templates/shop/product/edit_product");
        return modelAndView;
    }

    @RequestMapping({ "/shop/category/edit/page" })
    public ModelAndView editCategoryPage(ModelAndView modelAndView) {
        modelAndView.setViewName("admin/templates/shop/product/edit_category");
        return modelAndView;
    }

    @RequestMapping(value = { "/system_setting/getlogs" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result getLog(Integer page, Integer limit) {
        Result result = new Result();
        List<UserOperateLog> logs = this.userOperateLogService.getLogs(page, limit);
        result.setData(logs);
        result.setCount(this.userOperateLogService.getLogsCount());
        result.setStatus(Integer.valueOf(200));
        result.setMessage("日志获取成功!");
        return result;
    }

    @RequestMapping(value = { "/system_setting/get/parameter" }, method = { RequestMethod.GET })
    @ResponseBody
    public Result getSystemParam() {
        Result result = new Result();
        List<SystemParameter> systemParameter = this.systemParameterService.getSystemParameter();
        result.setData(systemParameter.get(0));
        result.setMessage("获取成功!");
        result.setStatus(Integer.valueOf(200));
        return result;
    }

    @RequestMapping(value = { "/system_setting/change/parameter" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result changeSystemParam(SystemParameter systemParameter) {
        Result result = new Result();
        Integer row = Integer.valueOf(0);
        if (systemParameter != null) {
            row = this.systemParameterService.changeSystemParameter(systemParameter);
        }
        result.setData(systemParameter);
        result.setCount(row);
        result.setMessage("修改成功!");
        result.setStatus(Integer.valueOf(200));
        return result;
    }
}
