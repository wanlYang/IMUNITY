/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.aspect;

import com.wanl.annotation.UserOperate;
import com.wanl.aspect.LogAspect;
import com.wanl.constant.ImunityConstant;
import com.wanl.entity.User;
import com.wanl.entity.UserOperateLog;
import com.wanl.service.UserOperateLogService;
import com.wanl.utils.IpUtil;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

@Aspect
public class LogAspect {
    @Autowired
    UserOperateLogService userOperateLogService;

    @Pointcut("execution(* com.wanl.*.controller.*.*(..))")
    public void controllerAspect() {
    }

    @Around("controllerAspect()")
    public Object aroundMethod(ProceedingJoinPoint proceedingJoinPoint) {
        Object result = null;
        Method method = null;
        long startTime = System.currentTimeMillis();
        long timeConsuming = 0L;
        UserOperateLog log = new UserOperateLog();

        try {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

            HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(ImunityConstant.REQUEST);

            HttpSession session = (HttpSession) requestAttributes.resolveReference(ImunityConstant.SESSION);

            SecurityContextImpl authentication = (SecurityContextImpl) session.getAttribute(ImunityConstant.SPRING_SECURITY_CONTEXT);
            String operator = null;
            if (authentication != null) {
                operator = authentication.getAuthentication().getName();
            }

            User user = (User) session.getAttribute(ImunityConstant.USER_SESSION);
            if (user != null) {
                operator = user.getUsername();
            }

            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat(ImunityConstant.YYYY_MM_DD_HH_MM_SS);

            Signature signature = proceedingJoinPoint.getSignature();

            MethodSignature methodSignature = (MethodSignature) signature;
            method = methodSignature.getMethod();
            String modelName = null;
            if (method != null) {
                if (method.isAnnotationPresent(UserOperate.class)) {
                    UserOperate userOperate = (UserOperate) method.getAnnotation(UserOperate.class);

                    modelName = userOperate.modelName();
                }
            }
            log.setRequestUrl(new String(request.getRequestURL()));
            log.setRequestMethod(request.getMethod());
            log.setIpAddress(IpUtil.getIpAddr(request));
            operator = (operator == null) ? ImunityConstant.ANONYMOUS : operator;
            log.setOperator(operator);
            log.setOperatingTime(dateFormat.parse(dateFormat.format(date)));
            modelName = (modelName == null) ? ImunityConstant.OTHER: modelName;
            log.setModelName(modelName);
            log.setIsAbnormal(ImunityConstant.NORMAL);

            result = proceedingJoinPoint.proceed();
            long endTime = System.currentTimeMillis();
            timeConsuming = endTime - startTime;
        } catch (Throwable e) {
            e.printStackTrace();
            long endTime = System.currentTimeMillis();
            timeConsuming = endTime - startTime;
            log.setIsAbnormal(ImunityConstant.ABNORMAL);
        }
        log.setTimeConsuming(timeConsuming);
        System.out.println("日志" + log);
        this.userOperateLogService.handlerLog(log);
        System.out.println("日志-----------------end.----------------------");
        return result;
    }
}
