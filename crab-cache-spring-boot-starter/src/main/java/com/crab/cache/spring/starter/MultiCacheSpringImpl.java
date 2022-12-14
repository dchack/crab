package com.crab.cache.spring.starter;

import com.crab.cache.multi.Item;
import com.crab.cache.multi.MultiCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.cache.support.NullValue;

import java.util.concurrent.Callable;

/**
 * Attention: do not support put absent, so do not support TransactionAwareCacheDecorator
 *
 * @author hackdc
 * @Date 2022/8/4 2:12 PM
 */
@Slf4j
public class MultiCacheSpringImpl extends AbstractValueAdaptingCache {

    private final MultiCache multiCache;

    private final String cacheName;

    protected MultiCacheSpringImpl(MultiCache multiCache, String cacheName) {
        super(true);
        this.multiCache = multiCache;
        this.cacheName = cacheName;
    }

    @Override
    protected Object lookup(Object key) {
        Item item = multiCache.get(String.valueOf(key));
        return fromStore(item);
    }

    private Object fromStore(Item item) {
        if (item != null) {
            if(item.isNull()) {
                return NullValue.INSTANCE;
            }
            return item.get();
        }
        return null;
    }

    @Override
    public String getName() {
        return cacheName;
    }

    @Override
    public Object getNativeCache() {
        return this;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        Object value = lookup(key);
        if (value != null) {
            return (T) value;
        }

        String lockKey = getKey(key).intern();
        // 写入锁
        synchronized (lockKey) {
            // double check
            value = lookup(key);
            if (value != null) {
                return (T) value;
            }

            try {
                value = valueLoader.call();
            } catch (Exception e) {
                log.error("MultiCacheSpringImpl get error", e);
            }
            put(key, toStoreValue(value));
            return (T) value;
        }
    }

    @Override
    public void put(Object key, Object value) {
        multiCache.put(String.valueOf(key),
                new Item<>(NullValue.INSTANCE == value ? null : value));
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        // ignore absent
        Object lookupValue = lookup(key);
        if (lookupValue == null){
            put(key, value);
        }
        return toValueWrapper(lookupValue);
    }

    @Override
    public void evict(Object key) {
        multiCache.remove(getKey(key));
    }

    @Override
    public void clear() {
        multiCache.removeAll();
    }

    private String getKey(Object key) {
        return String.valueOf(key);
    }
}
