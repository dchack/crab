package com.crab.cache.spring.starter;

import com.crab.cache.multi.MultiCacheBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * TODO
 *
 * @author hackdc
 * @Date 2022/8/15 5:06 PM
 */
@Configuration
@ConditionalOnClass(CacheResolver.class)
@EnableCaching
public class CrabCacheSpringConfiguration {

    @Bean
    public MultiCacheResolver multiCacheResolver(MultiCacheManage cacheManager, MultiCacheBuilder multiCacheBuilder) {
        return new MultiCacheResolver(cacheManager, multiCacheBuilder);
    }

    @Bean
    public MultiCacheManage cacheManager() {
        return new MultiCacheManage();
    }
}
