package com.crab.cache.spring.starter;

import com.crab.cache.multi.MultiCacheBuilder;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.SimpleCacheResolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CacheResolver implement
 * @author hackdc
 * @Date 2022/8/6 3:38 PM
 */
public class MultiCacheResolver extends SimpleCacheResolver {

    private MultiCacheBuilder multiCacheBuilder;

    private MultiCacheManage cacheManager;

    public MultiCacheResolver(MultiCacheManage multiCacheManage, MultiCacheBuilder multiCacheBuilder) {
        super(multiCacheManage);
        this.cacheManager = multiCacheManage;
        this.multiCacheBuilder = multiCacheBuilder;
    }

    @Override
    public Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> context) {
        Collection<String> cacheNames = getCacheNames(context);
        if (cacheNames == null) {
            return Collections.emptyList();
        }
        Collection<Cache> result = new ArrayList<>(cacheNames.size());
        for (String cacheName : cacheNames) {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache == null) {
                cache = cacheManager.computeIfAbsent(cacheName,
                        k -> new MultiCacheSpringImpl(multiCacheBuilder.build(
                                context.getMethod().getGenericReturnType(), cacheName,
                                context.getArgs().length == 0), cacheName));
            }
            result.add(cache);
        }
        return result;
    }
}
