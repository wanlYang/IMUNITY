package com.wanl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableCaching
@MapperScan("com.wanl.mapper")
@EnableConfigurationProperties
@EnableWebSecurity
@EnableAspectJAutoProxy(exposeProxy = true)
public class SpringMainApplicationStart {

    public static void main(String[] args) {
        SpringApplication.run(SpringMainApplicationStart.class, args);
    }

}
