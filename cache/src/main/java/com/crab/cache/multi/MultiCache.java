package com.crab.cache.multi;

import com.crab.cache.multi.cluster.ClusterStrategy;
import com.crab.cache.multi.cluster.Command;
import com.crab.cache.multi.config.MultiCacheProperties;
import com.crab.cache.multi.metric.MultiCacheStats;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * core class for multi cache API
 *
 * @author hackdc
 * @Date 2022/7/29 3:29 PM
 */
@Slf4j
public class MultiCache<T> {

    private static final Map<String, Object> key_Locks = new ConcurrentHashMap<>();

    private final CacheProvider<T> firstLevelCacheProvider;
    private final CacheProvider<T> secondLevelCacheProvider;

    private final CacheLoader<T> cacheLoader;

    private FirstLevelCache<T> firstLevelCache;

    private SecondLevelCache<T> secondLevelCache;

    private final ClusterStrategy clusterStrategy;

    private final MultiCacheStats multiCacheStats;

    private final String cacheName;

    private final boolean useCommonL1;

    private final MultiCacheProperties multiCacheProperties;

    private AtomicBoolean INITED = null;
    private ReentrantLock initBuildLock = null;

    public MultiCache(String cacheName, CacheProviderHolder<T> cacheProviderHolder, CacheLoader<T> cacheLoader,
                      MultiCacheStats multiCacheStats, MultiCacheProperties multiCacheProperties,
                      ClusterStrategy clusterStrategy, boolean useCommonL1) {
        this.cacheName = cacheName;
        this.firstLevelCacheProvider = cacheProviderHolder.getCacheL1Provider();
        this.secondLevelCacheProvider = cacheProviderHolder.getCacheL2Provider();
        this.cacheLoader = cacheLoader;
        this.multiCacheStats = multiCacheStats;
        this.multiCacheProperties = multiCacheProperties;
        this.clusterStrategy = clusterStrategy;
        this.useCommonL1 = useCommonL1;
        this.initBuildLock = new ReentrantLock();
        this.INITED = new AtomicBoolean(false);
    }

    public MultiCache<T> build(){
        initBuildLock.lock();
        try {
            if (!INITED.get()) {
                // init multi cache
                firstLevelCache = (FirstLevelCache<T>) firstLevelCacheProvider.build(transformL1CacheName(cacheName, useCommonL1));
                secondLevelCache = (SecondLevelCache<T>) secondLevelCacheProvider.build(cacheName);
                if (multiCacheProperties.isPreheat()) {
                    // store data to first cache from second cache
                    storeFirstCache();
                }
                // connect to cluster local cache
                clusterStrategy.connect();
                INITED.set(true);
            }
        }finally {
            initBuildLock.unlock();
        }
        return this;
    }

    public T get(String key) {
        CacheResult<T> cacheResult = getValue(key);

        if (multiCacheProperties.isRecord()) {
            multiCacheStats.record(cacheResult.getLevel());
        }
        return cacheResult.getValue() == null ? null : cacheResult.getValue().get();
    }

    public void put(String key, T value) {
        Item<T> item = new Item<>(value);
        firstLevelCache.set(key, item);
        secondLevelCache.set(key, item);
    }

    public void remove(String key) {
        clusterStrategy.publish(new Command().setKey(key).setCacheName(cacheName).setType(Command.EVICT_CMD));
        secondLevelCache.remove(key);
        firstLevelCache.remove(key);
    }

    public void removeAll() {
        clusterStrategy.publish(new Command().setCacheName(cacheName).setType(Command.CLEAR_CMD));
        secondLevelCache.removeAll();
        firstLevelCache.removeAll();
    }

    private CacheResult<T> getValue(String key) {
        CacheResult<T> cacheResult = new CacheResult<>(CacheResult.LEVEL_1);
        cacheResult.setValue(firstLevelCache.get(key));
        if (cacheResult.getValue() != null) {
            return cacheResult;
        }
        synchronized (key_Locks.computeIfAbsent(key, v -> new Object())) {
            try {
                cacheResult.setValue(firstLevelCache.get(key));
                if (cacheResult.getValue() != null) {
                    return cacheResult;
                }
                cacheResult.setValue(secondLevelCache.get(key));
                if (cacheResult.getValue() != null) {
                    cacheResult.setLevel(CacheResult.LEVEL_2);
                    firstLevelCache.set(key, cacheResult.getValue());
                }else {
                    cacheResult.setLevel(CacheResult.LEVEL_OUTER);
                    if (cacheLoader != null) {
                        cacheResult.setValue(new Item<>(cacheLoader.load(key)));
                        // todo null value
                        if (cacheResult.getValue() != null) {
                            firstLevelCache.set(key, cacheResult.getValue());
                            secondLevelCache.set(key, cacheResult.getValue());
                        }
                    }
                }
            } finally {
                key_Locks.remove(key);
            }
        }
        return cacheResult;
    }

    private void storeFirstCache() {
        Map<String, Item<T>> preheatMap = secondLevelCache.getPreheatMap();
        if (MapUtils.isNotEmpty(preheatMap)) {
            firstLevelCache.fill(preheatMap);
            log.info("Multi cache init, fill first cache success : {}, {}", cacheName, preheatMap.size());
        }
    }

    private String transformL1CacheName(String cacheName, boolean useCommonL1) {
        return useCommonL1 ? "common" : cacheName;
    }

}
