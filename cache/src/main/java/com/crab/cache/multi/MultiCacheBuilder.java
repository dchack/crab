package com.crab.cache.multi;

import com.crab.cache.multi.cluster.ClusterStrategy;
import com.crab.cache.multi.config.MultiCacheProperties;
import com.crab.cache.multi.config.MultiCachePropertiesResolver;
import com.crab.cache.multi.metric.MultiCacheMetric;
import com.crab.cache.multi.metric.MultiCacheStats;
import com.crab.util.HostNameUtil;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 *
 * Multi cache init builder
 *
 * @author hackdc
 * @Date 2022/7/30 10:48 AM
 */
@Slf4j
public class MultiCacheBuilder<T> {

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private CacheProviderHolder<T> cacheProviderHolder;

    @Autowired
    private ClusterStrategy clusterStrategy;

    @Autowired
    private Environment environment;

    private Type type;

    private boolean useCommonL1;

    /**
     * build a multi cache instance
     * @param cacheName
     * @param cacheLoader
     * @return
     */
    public MultiCache<T> build(String cacheName, CacheLoader<T> cacheLoader) {
        MultiCache<T> multiCache = null;
        if (StringUtils.isBlank(cacheName)) {
            log.error("multi cache init failed, argument is illegality");
            throw new IllegalArgumentException();
        }
        MultiCacheProperties multiCacheProperties = MultiCachePropertiesResolver.resolve(environment, cacheName);
        cacheProviderHolder.start(cacheName, getType(cacheLoader), multiCacheProperties);
        MultiCacheStats multiCacheStats = new MultiCacheStats();
        multiCache = new MultiCache<T>(cacheName, cacheProviderHolder, cacheLoader, multiCacheStats,
                multiCacheProperties, clusterStrategy, useCommonL1).build();
        // add metric
        if (multiCacheProperties.isRecord()){
            new MultiCacheMetric(Tags.of("ip", HostNameUtil.getIp(), "name", cacheName), multiCacheStats).bindTo(meterRegistry);
        }
        return multiCache;
    }

    public MultiCache<T> build(Type type, String bizName) {
        this.type = type;
        return build(bizName, null);
    }

    public MultiCache<T> build(Type type, String bizName, boolean useCommonL1) {
        this.type = type;
        this.useCommonL1 = useCommonL1;
        return build(bizName, null);
    }

    private Type getType(CacheLoader<T> cacheLoader) {
        if (cacheLoader != null) {
            Type superClass = cacheLoader.getClass().getGenericSuperclass();
            return ((ParameterizedType)superClass).getActualTypeArguments()[0];
        }
        return type;
    }

}
