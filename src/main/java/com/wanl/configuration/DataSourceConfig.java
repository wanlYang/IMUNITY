/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.configuration;

import com.wanl.datasource.MultipleDataSource;
import com.wanl.jdbc.PackagesSqlSessionFactoryBean;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Administrator
 * @Description 数据源配置 mybatis
 * @Date 9:30 2020-12-31
 * @Param
 * @return
 **/
@Configuration
public class DataSourceConfig {
    private static final Logger logger = LogManager.getLogger(DataSourceConfig.class.getName());
    @Autowired
    @Qualifier("mysqlConfigDataSource")
    private DataSource mysqlConfigDataSource;

    @Autowired
    @Qualifier("mysqlDeveDataSource")
    private DataSource mysqlDeveDataSource;
    @Autowired
    private MultipleDataSource multipleDataSource;
    @Bean(name = { "mysqlConfigDataSource" })
    @ConfigurationProperties(prefix = "spring.datasource.configdatasource")
    public BasicDataSource mysqlConfigDataSource(){
        return DataSourceBuilder.create().type(BasicDataSource.class).build();
    }
    @Bean(name = { "mysqlDeveDataSource" })
    @ConfigurationProperties(prefix = "spring.datasource.developmentdatasource")
    public BasicDataSource mysqlDeveDataSource() {

        return DataSourceBuilder.create().type(BasicDataSource.class).build();
    }

    @Bean
    public MultipleDataSource multipleDataSource() {
        MultipleDataSource multipleDataSource = new MultipleDataSource();
        multipleDataSource.setDefaultTargetDataSource(this.mysqlDeveDataSource);
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("mysqlDeveDataSource", this.mysqlDeveDataSource);
        targetDataSources.put("mysqlConfigDataSource", this.mysqlConfigDataSource);
        multipleDataSource.setTargetDataSources(targetDataSources);
        return multipleDataSource;
    }

    @Bean(name = { "sqlSessionFactory" })
    public SqlSessionFactory sqlSessionFactoryBean() {
        try {
            PackagesSqlSessionFactoryBean sqlSessionFactoryBean = new PackagesSqlSessionFactoryBean();
            sqlSessionFactoryBean.setDataSource(this.multipleDataSource);
            sqlSessionFactoryBean.setTypeAliasesPackage("com.wanl.**.entity");
            PathMatchingResourcePatternResolver mapperLocations = new PathMatchingResourcePatternResolver();
            Resource[] resources = mapperLocations.getResources("classpath*:/mapper/*Mapper.xml");
            sqlSessionFactoryBean.setMapperLocations(resources);
            logger.info("mybatis sqlSessionFactoryBean success create ");
            return sqlSessionFactoryBean.getObject();
        } catch (IOException e) {
            logger.error("mybatis resolver mapper*xml is error");
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            logger.error("mybatis sqlSessionFactoryBean create error");
            e.printStackTrace();
            return null;
        }
    }
    @Bean
    public PlatformTransactionManager platformTransactionManager() {
        return new DataSourceTransactionManager(this.multipleDataSource);
    }
}
