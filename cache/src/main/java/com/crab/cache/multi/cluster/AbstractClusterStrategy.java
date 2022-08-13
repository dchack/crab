package com.crab.cache.multi.cluster;

import com.crab.cache.multi.Cache;
import com.crab.cache.multi.CacheProviderHolder;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author hackdc
 * @Date 2022/8/7 4:29 PM
 */
public abstract class AbstractClusterStrategy implements ClusterStrategy{

    @Autowired
    private CacheProviderHolder cacheProviderHolder;

    @Override
    public void handleCommand(Command cmd) {
        if (isLocalCommand(cmd)) {
            return;
        }
        String cacheName = cmd.getCacheName();
        String key = cmd.getKey();
        Cache cache = cacheProviderHolder.getCacheL1Provider().get(cacheName);
        switch (cmd.getType()) {
            case Command.EVICT_CMD:
                cache.remove(key);
                break;
            case Command.CLEAR_CMD:
                cache.removeAll();
                break;
        }
    }

}
