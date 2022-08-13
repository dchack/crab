package com.crab.cache.multi;

import com.crab.cache.multi.caffeine.CaffeineCacheProvider;
import com.crab.cache.multi.config.CaffeinePropertiesResolver;
import com.crab.cache.multi.config.MultiCacheProperties;
import com.crab.cache.multi.config.RedisPropertiesResolver;
import com.crab.cache.multi.redis.RedisCacheProvider;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author hackdc
 * @Date 2022/8/6 6:02 PM
 */
public class CacheProviderHolder<T> {

    private CacheProvider<T> cacheL1Provider;

    private CacheProvider<T> cacheL2Provider;

    private Environment environment;

    private RedisTemplate<String, String> redisTemplate;

    private String applicationName;

    private AtomicBoolean STARTED;

    public CacheProviderHolder(Environment environment, RedisTemplate<String, String> redisTemplate, String applicationName) {
        this.environment = environment;
        this.redisTemplate = redisTemplate;
        this.applicationName = applicationName;
        this.STARTED = new AtomicBoolean(false);
    }

    public void start(String cacheName, Type type, MultiCacheProperties multiCacheProperties) {
        String bucketKey = applicationName + ":cache:" + cacheName;
        String cacheL1Name = multiCacheProperties.getL1();
        String cacheL2Name = multiCacheProperties.getL2();
        synchronized (this) {
            if (!STARTED.get()) {
                if ("caffeine".equals(cacheL1Name)) {
                    cacheL1Provider = new CaffeineCacheProvider<T>(CaffeinePropertiesResolver.resolve(environment, cacheName));
                }
                STARTED.compareAndSet(false, true);
            }
        }
        // because redis use type to cast object, so need a new provider to hold type
        if ("redis".equals(cacheL2Name)) {
            cacheL2Provider = new RedisCacheProvider<T>(redisTemplate, bucketKey, type,
                    RedisPropertiesResolver.resolve(environment, cacheName));
        }
    }

    public CacheProvider<T> getCacheL1Provider() {
        return cacheL1Provider;
    }

    public CacheProvider<T> getCacheL2Provider() {
        return cacheL2Provider;
    }
}
