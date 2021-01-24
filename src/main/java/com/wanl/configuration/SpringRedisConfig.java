/**
 * @Yang
 *
 * Copyright (c) 2020 yang www.theaic.com 344295704@qq.com
 *
 */

package com.wanl.configuration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * @Author Administrator
 * @Description //redis配置
 * @Date 10:53 2021-01-23
 * @Param
 * @return
 **/
@Configuration
public class SpringRedisConfig extends CachingConfigurerSupport {


    @Bean(name = "redisCacheKeyGenerator")
    @Primary
    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(method.getName());
            for (Object obj : params) {
                sb.append(obj.toString());
            }
            return sb.toString();
        };
    }

    /**
     * 缓存配置管理器
     *
     * @param redisConnectionFactory the redis connection factory
     * @return the cache manager
     * @author leiguoqing
     * @date 2020 -07-23 21:43:28
     */
    /**
     * 缓存配置管理器
     *
     * @param redisConnectionFactory the redis connection factory
     * @return the cache manager
     * @author leiguoqing
     * @date 2020 -07-23 21:43:28
     */
    @Bean(name = "CacheManager")
    @Primary
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        // 以锁写入的方式创建RedisCacheWriter对象
        RedisCacheWriter writer = RedisCacheWriter.lockingRedisCacheWriter(redisConnectionFactory);

        // 设置 CacheManager 的 Value 序列化方式为 Jackson2JsonRedisSerialize, RedisCacheConfiguration 默认就是使用 StringRedisSerializer序列化key，
        // JdkSerializationRedisSerializer 序列化 value
        RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer());
        // 创建默认缓存配置对象
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(pair).entryTtl(Duration.ofMinutes(5));
        return new RedisCacheManager(writer, config);
    }

    /**
     * redisTemplate 序列化默认使用的 JdkSerializationRedisSerializer, 存储二进制字节码，这里改为使用 jackson2JsonRedisSerializer 自定义序列化
     * 想了解 SpringBoot 是如何默认使用 JdkSerializationRedisSerializer 的，看这里：<a href='https://www.cnblogs.com/HuuuWnnn/p/11864380.html'>SpringBoot项目使用RedisTemplate设置序列化方式</a>
     * <br/>
     * StringRedisTemplate 使用的是 StringRedisSerializer，不受影响，不用重新配置
     * <br/>
     * 相关文章：<br/>
     * <ul>
     *     <li>
     *         <a href='https://blog.csdn.net/m0_37893932/article/details/78259288'>Spring-boot通过redisTemplate使用redis(无须手动序列化)</a>
     *     </li>
     *     <li>
     *         <a href='https://www.cnblogs.com/wangzhuxing/p/10198347.html'>redisTemplate和stringRedisTemplate对比、redisTemplate几种序列化方式比较</a>
     *     </li>
     * </ul>
     *
     * <br>创建人： leigq
     * <br>创建时间： 2018-11-08 10:12
     * <br>
     *
     * @param redisConnectionFactory redis连接工厂
     * @return RedisTemplate
     */
    @Bean
    @Primary
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 使用 Jackson2JsonRedisSerialize 替换默认序列化
        // 以下代码为将 RedisTemplate 的 Value 序列化方式由 JdkSerializationRedisSerializer更换为 Jackson2JsonRedisSerializer
        // 此种序列化方式结果清晰、容易阅读、存储字节少、速度快，所以推荐更换
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer());
        // 设置 key 的序列化规则
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer());
        redisTemplate.setStringSerializer(new StringRedisSerializer());
        // 是否启用事务
        // redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
    /**
     * Jackson 2 json redis serializer jackson 2 json redis serializer.
     *
     * @return the jackson 2 json redis serializer
     * @author leiguoqing
     * @date 2020 -07-25 14:31:07
     */
    private Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer() {
        // 使用 Jackson2JsonRedisSerialize 替换默认序列化
        return new Jackson2JsonRedisSerializer<>(Object.class);
    }
}
