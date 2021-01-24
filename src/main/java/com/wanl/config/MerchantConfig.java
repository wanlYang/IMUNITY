package com.wanl.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @ClassName MerchantConfig
 * @Description 配置文件类 映射信息
 * @Author Administrator
 * @Date 2021-01-23 11:30
 * @Version 2.0
 **/

/*指定配置文件名，默认从classpath下寻找该文件，也就是等同于classpath:dataSource.properties
 * 可以指定多个文件
 */
@Configuration
@ConfigurationProperties(prefix = "merchant",ignoreUnknownFields = false)
@PropertySource(value = {"classpath:/config/publicmerchant.properties"})

/*
 * 指定前缀，读取的配置信息项必须包含该前缀，且除了前缀外，剩余的字段必须和实体类的属性名相同，
 * 才能完成银映射
 */
@Data
@Component
public class MerchantConfig {
    private String defaultUserImg;
    private String tempUserPath;
    private String tempArticlePath;
    private String tempProductPath;
    private String imunityWatermark;
    private String DEFAUTLHEADIMG;
    private String redirectQQUrl;
    private String ACCESSTOKEN_URI;
    private String GRANTTYPE;
    private String CLIENTID;
    private String CLIENTSECRET;
    private String REDIRECTURI;
    private String OPENIDURI;
    private String TOKENACCESSAPI;
    private String USERINFOURI;
    private String USERINFOAPI;
}
