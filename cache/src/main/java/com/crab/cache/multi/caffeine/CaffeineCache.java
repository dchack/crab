package com.crab.cache.multi.caffeine;

import com.crab.cache.multi.FirstLevelCache;
import com.crab.cache.multi.Item;
import com.github.benmanes.caffeine.cache.Cache;

import java.util.Map;

/**
 * caffeine cache impl
 *
 * @author hackdc
 * @Date 2022/7/29 5:08 PM
 */
public class CaffeineCache<T> implements FirstLevelCache<T> {

    private Cache<String, Item<T>> cache;

    public void setCache(Cache<String, Item<T>> cache) {
        this.cache = cache;
    }

    @Override
    public Item<T> get(String key) {
        return cache.getIfPresent(key);
    }

    @Override
    public void set(String key, Item<T> value) {
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
    public void fill(Map<String, Item<T>> values) {
        cache.putAll(values);
    }


}
