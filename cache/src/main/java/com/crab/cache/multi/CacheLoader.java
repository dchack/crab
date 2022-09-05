package com.crab.cache.multi;

/**
 * Developer impl this class provide data
 * from DB source for store to multi cache
 *
 * @author hackdc
 * @Date 2022/7/30 2:24 PM
 */
public abstract class CacheLoader<T> {

    public abstract T load(String key);
}
