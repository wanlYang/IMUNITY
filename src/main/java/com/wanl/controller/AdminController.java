/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.controller;

import com.wanl.annotation.UserOperate;
import com.wanl.config.MerchantConfig;
import com.wanl.constant.ImunityConstant;
import com.wanl.controller.AdminController;
import com.wanl.entity.OpenUser;
import com.wanl.entity.Result;
import com.wanl.entity.User;
import com.wanl.service.UserService;
import com.wanl.utils.CookieUtil;
import com.wanl.utils.SpringSecurityUtil;
import com.wanl.utils.UrlUtil;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({ "/admin" })
public class AdminController {
    private static Logger logger = LogManager.getLogger(AdminController.class.getName());

    @Autowired
    private UserService userService;

    @Autowired
    private MerchantConfig merchantConfig;


    @RequestMapping({ "/login" })
    @UserOperate(modelName = "管理员登陆")
    public ModelAndView login(@RequestParam(value = "error", required = false) String error, ModelAndView modelAndView,
            HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (error != null) {
            modelAndView.addObject("error", "登陆失败!无效的token");
        }
        if (authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken) {
            modelAndView.setViewName("admin/login/login");
            return modelAndView;
        }
        modelAndView.setViewName("redirect:" + request.getContextPath() + "/admin/manager/index");
        return modelAndView;
    }

    @RequestMapping({ "/sessiontimeout" })
    public void sessionTimeout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getHeader("X-Requested-With") != null
                && request.getHeader(ImunityConstant.X_REQUEST_WITH).equalsIgnoreCase(ImunityConstant.XML_HTTP_REQUEST)) {
            logger.info("AdminController.sessionTimeout() ajax超时" + request.getHeader(ImunityConstant.X_REQUEST_WITH));
            PrintWriter writer = response.getWriter();

            String timeout = Result.failed(Integer.valueOf(10235), "timeout");
            writer.write(timeout);
            writer.close();
        } else {
            logger.info("AdminController.sessionTimeout()");
            response.sendRedirect(request.getContextPath() + "/");
        }
    }

    @RequestMapping({ "/info" })
    public ModelAndView info(ModelAndView modelAndView, Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        User user = this.userService.getUser(principal.getId());
        OpenUser openUserQQ = this.userService.getOpenUserByUserId(user.getId(), "QQ");
        if (openUserQQ != null) {
            modelAndView.addObject(ImunityConstant.ADMIN_OPEN_QQ, openUserQQ);
        }
        modelAndView.addObject(ImunityConstant.ADMIN_REQUEST, user);
        modelAndView.setViewName("admin/templates/admin/admin_info");
        return modelAndView;
    }

    @ResponseBody
    @RequestMapping(value = { "/submit/change/info" }, method = { RequestMethod.POST })
    public Result changeInfo(User user, HttpServletRequest request) {
        Result result = new Result();
        User userAfter = this.userService.updateAdmin(user);
        SpringSecurityUtil.updateSecurityContextImplAuthentication(userAfter);
        result.setData(userAfter);
        result.setMessage("修改成功!");
        result.setStatus(Integer.valueOf(200));
        return result;
    }

    @ResponseBody
    @RequestMapping(value = { "/upload/uploadImg" }, method = { RequestMethod.POST })
    public Map<String, Object> changeImg(HttpServletRequest request, @RequestParam("file") MultipartFile file)
            throws IllegalStateException, IOException {
        Authentication authentication = SpringSecurityUtil.getSecurityAuthentication();

        User principal = (User) authentication.getPrincipal();
        User user = this.userService.getUser(principal.getId());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");
        String rootPath = request.getServletContext().getRealPath("/admin/uploads/");
        String res = sdf.format(new Date());
        String originalFilename = file.getOriginalFilename();
        String newFileName = res + originalFilename.substring(originalFilename.lastIndexOf("."));
        Calendar calendar = Calendar.getInstance();

        File dateDirs = new File(calendar.get(1) + File.separator + (calendar.get(2) + 1));
        File newFile = new File(rootPath + File.separator + dateDirs + File.separator + newFileName);
        if (!newFile.getParentFile().exists()) {
            newFile.getParentFile().mkdirs();
        }
        file.transferTo(newFile);

        String fileUrl = "/admin/uploads/" + calendar.get(1) + "/" + (calendar.get(2) + 1) + "/" + newFileName;

        Map<String, Object> map = new HashMap<String, Object>();
        user.setHeadImg(fileUrl);

        SpringSecurityUtil.updateSecurityContextImplAuthentication(user);

        Integer updateAdminHeadImg = this.userService.updateAdminHeadImg(fileUrl, user.getId());
        if (updateAdminHeadImg.intValue() != 0) {
            map.put("code", Integer.valueOf(0));
            map.put("msg", "上传成功!如果未生效!下次登陆生效!");
        } else {
            map.put("msg", "上传失败!");
            map.put("code", Integer.valueOf(-1));
        }

        Map<String, Object> mapData = new HashMap<String, Object>();
        mapData.put("src", request.getContextPath() + fileUrl);
        mapData.put("title", newFileName);
        map.put("data", mapData);
        return map;
    }

    @RequestMapping({ "/open/oauth2/qq" })
    public void openOauth2QQLogin(HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        try {
            int indexOf = this.merchantConfig.getRedirectQQUrl().indexOf("https://www.theaic.cn/");
            int lastIndexOf = this.merchantConfig.getRedirectQQUrl().lastIndexOf("&state=test");

            String substring = this.merchantConfig.getRedirectQQUrl().substring(indexOf, lastIndexOf);

            String redirectQQUrlEncoder = this.merchantConfig.getRedirectQQUrl().replace(substring, UrlUtil.getURLEncoderString(substring));
            response.sendRedirect(redirectQQUrlEncoder);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }

    @RequestMapping(value = { "/open/oauth2/qq/success" }, method = { RequestMethod.GET })
    public ModelAndView openOauth2QQLoginSuccess(ModelAndView modelAndView, HttpServletResponse response) {
        CookieUtil.addCookie(response, ImunityConstant.QQ_OAUTH2_SUCCESS,ImunityConstant.SUCCESS, Integer.valueOf(604800), true);
        modelAndView.addObject("isRefresh", Boolean.valueOf(true));
        modelAndView.addObject("message", "授权成功!下次即可使用QQ登陆!");
        modelAndView.setViewName("admin/templates/info");
        return modelAndView;
    }

    @RequestMapping(value = { "/open/oauth2/qq/cancel" }, method = { RequestMethod.GET })
    public ModelAndView cancelOpenOauth2QQLogin(ModelAndView modelAndView, Authentication authentication,
            HttpServletResponse response, HttpServletRequest request) {
        Object principal = authentication.getPrincipal();
        if (authentication.isAuthenticated() && principal instanceof User) {
            User user = (User) principal;
            boolean isCancel = this.userService.cancelOpenOauth(user.getId(), "QQ");
            if (isCancel) {
                modelAndView.addObject("message", "取消授权成功!");
                modelAndView.addObject("isRefresh", Boolean.valueOf(true));

                CookieUtil.delCookie(response, request, "QQ_OAUTH2_SUCCESS");
            } else {
                modelAndView.addObject("message", "取消授权失败!");
            }
        }
        modelAndView.setViewName("admin/templates/info");
        return modelAndView;
    }

    @RequestMapping(value = { "/open/oauth2/qq/success/already" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result openOauth2QQLoginSuccessAlready(@RequestParam("userId") String userId) {
        Result result = new Result();
        boolean isAuth = this.userService.checkAuthorizationOpenInfo(userId, "QQ");
        if (isAuth) {
            result.setMessage("已经认证@");
            result.setStatus(Integer.valueOf(-1));
            return result;
        }
        result.setMessage("未认证@");
        result.setStatus(Integer.valueOf(-2));
        return result;
    }

    @RequestMapping({ "/change_pass" })
    public ModelAndView changePassword(ModelAndView modelAndView, Authentication authentication) {
        if (authentication.getPrincipal() instanceof User) {
            User principal = (User) authentication.getPrincipal();
            User user = this.userService.getUser(principal.getId());
            modelAndView.addObject(ImunityConstant.ADMIN_REQUEST, user);
            modelAndView.setViewName("admin/templates/admin/password");
        }
        return modelAndView;
    }

    @RequestMapping(value = { "/check/old/password" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result checkOldPass(String oldPass, Authentication authentication) {
        Result result = new Result();
        if (authentication.getPrincipal() instanceof User) {
            User principal = (User) authentication.getPrincipal();
            User user = this.userService.getUser(principal.getId());
            boolean isOk = this.userService.checkOldPassword(user, oldPass);
            if (isOk) {
                result.setStatus(Integer.valueOf(200));
                result.setMessage("旧密码输入正确!");
            } else {
                result.setStatus(Integer.valueOf(-1));
                result.setMessage("旧密码输入错误!");
            }
        }
        return result;
    }

    @RequestMapping(value = { "/submit/change/password" }, method = { RequestMethod.POST })
    @ResponseBody
    public Result checkPassword(String oldPass, String newPass, Authentication authentication) {
        Result result = new Result();
        if (authentication.getPrincipal() instanceof User) {
            User principal = (User) authentication.getPrincipal();
            User user = this.userService.getUser(principal.getId());
            boolean isCorrect = this.userService.checkOldPassword(user, oldPass);
            if (isCorrect) {
                boolean isSuccess = this.userService.changePassword(newPass, user);
                if (isSuccess) {
                    result.setMessage("密码修改成功!");
                    result.setStatus(Integer.valueOf(200));
                    return result;
                }
            } else {
                result.setMessage("旧密码输入错误!");
                result.setStatus(Integer.valueOf(-1));
            }
        }
        return result;
    }

    @RequestMapping({ "/get/info" })
    @ResponseBody
    public Object testinfo(Authentication authentication, HttpServletResponse response) {
        return authentication.getPrincipal();
    }
}
