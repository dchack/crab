package com.crab.cache.multi.redis;


import com.crab.cache.multi.Cache;
import com.crab.cache.multi.CacheProvider;
import com.crab.cache.multi.config.RedisProperties;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author hackdc
 * @Date 2022/7/30 9:22 AM
 */

public class RedisCacheProvider<T> implements CacheProvider<T> {

    private RedisTemplate<String, String> redisTemplate;

    private String bucketKey;

    /**
     * redis need this type for Deserialize
     */
    private Type type;

    private RedisProperties redisProperties;

    private final Map<String, Cache<T>> redisCacheMap = new ConcurrentHashMap<>();

    public RedisCacheProvider(RedisTemplate<String, String> redisTemplate, String bucketKey, Type type,
                              RedisProperties redisProperties) {
        this.redisTemplate = redisTemplate;
        this.bucketKey = bucketKey;
        this.type = type;
        this.redisProperties = redisProperties;
    }

    @Override
    public String getName() {
        return "redis";
    }

    @Override
    public Cache<T> build(String cacheName) {
        Cache<T> cache = new RedisCache<T>(redisTemplate, type, bucketKey, redisProperties);
        redisCacheMap.put(cacheName, cache);
        return cache;
    }

    @Override
    public Cache<T> get(String cacheName) {
        return redisCacheMap.get(cacheName);
    }

}
