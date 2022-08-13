package com.crab.cache.multi;

/**
 *
 * @author hackdc
 * @Date 2022/7/29 5:50 PM
 */
public interface CacheProvider<T> {

    /**
     * get cache name
     * @return
     */
    String getName();

    /**
     * build a cache
     * @return
     */
    Cache<T> build(String cacheName);

    /**
     * get cache instance
     * @param cacheName
     * @return
     */
    Cache<T> get(String cacheName);

}
