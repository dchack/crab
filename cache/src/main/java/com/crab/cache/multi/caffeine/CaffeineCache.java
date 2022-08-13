package com.crab.cache.multi.caffeine;

import com.crab.cache.multi.FirstLevelCache;
import com.github.benmanes.caffeine.cache.Cache;

import java.util.Map;

/**
 * caffeine cache impl
 *
 * @author hackdc
 * @Date 2022/7/29 5:08 PM
 */
public class CaffeineCache<T> implements FirstLevelCache<T> {

    private Cache<String, T> cache;

    public void setCache(Cache<String, T> cache) {
        this.cache = cache;
    }

    @Override
    public T get(String key) {
        return cache.getIfPresent(key);
    }

    @Override
    public void set(String key, T value) {
        cache.put(key, value);
    }

    @Override
    public void remove(String key) {
        cache.invalidate(key);
    }

    @Override
    public void removeAll() {
        cache.invalidateAll();
    }

    @Override
    public void fill(Map<String, T> values) {
        cache.putAll(values);
    }


}
