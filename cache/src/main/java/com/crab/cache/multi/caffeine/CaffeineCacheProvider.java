package com.crab.cache.multi.caffeine;

import com.crab.cache.multi.Cache;
import com.crab.cache.multi.CacheProvider;
import com.crab.cache.multi.config.CaffeineProperties;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * caffeine provider
 *
 * @author hackdc
 * @Date 2022/7/29 5:58 PM
 */
public class CaffeineCacheProvider<T> implements CacheProvider<T> {

    private CaffeineProperties caffeineProperties;

    private final Map<String, Cache<T>> caffeineCacheMap = new ConcurrentHashMap<>();

    public CaffeineCacheProvider(CaffeineProperties caffeineProperties) {
        this.caffeineProperties = caffeineProperties;
    }

    @Override
    public String getName() {
        return "caffeine";
    }

    @Override
    public Cache<T> build(String cacheName) {
        Cache<T> cache = caffeineCacheMap.get(cacheName);

        if (cache != null) {
            return cache;
        }

        CaffeineCache<T> caffeineCache = new CaffeineCache<>();
        Caffeine<Object, Object> cacheBuilder = Caffeine.newBuilder();
        cacheBuilder.maximumSize(caffeineProperties.getSize());
        cacheBuilder.expireAfterWrite(caffeineProperties.getExpire(), TimeUnit.MINUTES);
        caffeineCache.setCache(cacheBuilder.build());
        caffeineCacheMap.put(cacheName, caffeineCache);
        return caffeineCache;
    }

    @Override
    public Cache<T> get(String cacheName) {
        return caffeineCacheMap.get(cacheName);
    }

}
