/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.aspect;

import com.wanl.annotation.SwitchingDataSource;
import com.wanl.aspect.MultipleDataSourceAspect;
import com.wanl.datasource.MultipleDataSource;
import java.lang.reflect.Method;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MultipleDataSourceAspect {
    private static final Logger logger = LogManager.getLogger(MultipleDataSourceAspect.class.getName());

    @Pointcut("execution(* com.wanl.service..*.*ServiceImpl.*(..))")
    public void interceptorService() {
    }

    @Before("interceptorService()")
    public void setDataSource(JoinPoint joinPoint) {
        Object target = joinPoint.getTarget();
        if (target.getClass().isAnnotationPresent(SwitchingDataSource.class)) {
            SwitchingDataSource switchingDataSource = (SwitchingDataSource) target.getClass()
                    .getAnnotation(SwitchingDataSource.class);
            String dataSource = switchingDataSource.value();
            logger.info(target.getClass() + "数据源切换至--->" + dataSource);
            MultipleDataSource.setDataSourceKey(dataSource);
        } else {
            logger.info("默认数据源操作--->" + target.getClass());
        }

        Signature signature = joinPoint.getSignature();

        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method != null) {
            if (method.isAnnotationPresent(SwitchingDataSource.class)) {
                SwitchingDataSource switchingDataSource = (SwitchingDataSource) method
                        .getAnnotation(SwitchingDataSource.class);

                String dataSource = switchingDataSource.value();
                logger.info(methodSignature.getName() + "数据源切换至--->" + dataSource);
                MultipleDataSource.setDataSourceKey(dataSource);
            }
        }
    }

    @After("interceptorService()")
    public void removeDataSource() {
        MultipleDataSource.removeDataSourceKey();
    }
}
