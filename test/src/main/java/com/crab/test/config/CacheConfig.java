package com.crab.test.config;

import com.crab.cache.multi.CacheLoader;
import com.crab.cache.multi.MultiCache;
import com.crab.cache.multi.MultiCacheBuilder;
import com.crab.test.dto.UserInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * TODO
 *
 * @author hackdc
 * @Date 2022/8/14 10:05 AM
 */
@Configuration
public class CacheConfig {

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration =new RedisStandaloneConfiguration("localhost", 55002);
        redisStandaloneConfiguration.setPassword("redispw");
        redisStandaloneConfiguration.setUsername("default");
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(stringSerializer);
        return redisTemplate;
    }

    @Bean
    public MultiCache userInfoMultiCache(MultiCacheBuilder multiCacheBuilder) {
        return multiCacheBuilder.build("userInfo", new CacheLoader<UserInfo>() {
            @Override
            public UserInfo load(String key) {
                UserInfo userInfo = new UserInfo();
                userInfo.setUserId(Long.valueOf(key));
                userInfo.setName("test");
                userInfo.setWeight(120);
                userInfo.setHeadUrl("/static/head.jpg");
                return userInfo;
            }
        });
    }
}
