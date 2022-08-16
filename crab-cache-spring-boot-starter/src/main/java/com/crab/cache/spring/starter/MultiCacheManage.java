package com.crab.cache.spring.starter;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.AbstractCacheManager;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * CacheManage implement
 *
 * @author hackdc
 * @Date 2022/8/15 11:23 PM
 */
public class MultiCacheManage implements CacheManager {

    private final Map<String, Cache> cacheMap = new ConcurrentHashMap<>();

    public Cache computeIfAbsent(String cacheName, Function<? super String, ? extends Cache> mappingFunction) {
        return cacheMap.computeIfAbsent(cacheName, mappingFunction);
    }

    @Override
    public Cache getCache(String name) {
        return cacheMap.get(name);
    }

    @Override
    public Collection<String> getCacheNames() {
        return cacheMap.keySet();
    }
}
