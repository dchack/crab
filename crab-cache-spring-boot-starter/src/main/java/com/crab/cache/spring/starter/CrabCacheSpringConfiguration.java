package com.crab.cache.spring.starter;

import com.crab.cache.multi.MultiCacheBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * auto configuration for crab cache
 *
 * @author hackdc
 * @Date 2022/8/15 5:06 PM
 */
@Configuration
@ConditionalOnClass({CacheResolver.class, CacheManager.class})
@EnableCaching
public class CrabCacheSpringConfiguration extends CachingConfigurerSupport {

    private final MultiCacheBuilder multiCacheBuilder;

    public CrabCacheSpringConfiguration(MultiCacheBuilder multiCacheBuilder) {
        this.multiCacheBuilder = multiCacheBuilder;
    }

    @Bean
    @Override
    public MultiCacheResolver cacheResolver() {
        return new MultiCacheResolver(new MultiCacheManage(), multiCacheBuilder);
    }

}
