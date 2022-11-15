package com.crab.cache.multi.cluster;

import com.crab.cache.multi.Cache;
import com.crab.cache.multi.CacheProviderHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ConcurrentSkipListSet;

/**
 *
 * @author hackdc
 * @Date 2022/8/7 4:29 PM
 */
@Slf4j
public abstract class AbstractClusterStrategy implements ClusterStrategy{

    /**
     * Records handled commands
     */
    private static ConcurrentSkipListSet<String> HANDLED_CMD_SET = new ConcurrentSkipListSet<>();

    @Autowired
    private CacheProviderHolder cacheProviderHolder;

    @Override
    public void handleCommand(Command cmd) {
        // If command is from local, skip it
        if (isLocalCommand(cmd)) {
            return;
        }
        // If command is a repeated request, skip it
        if (isRepeatedCmd(cmd)) {
            log.warn("Repeated cmd: {}, {}", cmd.getKey(), cmd.getType());
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

    /**
     * Confirm command is from local or not, subclass implement.
     * @param cmd
     * @return
     */
    protected abstract boolean isLocalCommand(Command cmd);

    /**
     * determine whether this is a repeated command
     * @param command
     * @return
     */
    protected boolean isRepeatedCmd(Command command) {
        String handledCmdKey = command.getUniqueIdentification();
        if (HANDLED_CMD_SET.contains(handledCmdKey)) {
            return true;
        }
        HANDLED_CMD_SET.add(handledCmdKey);
        return false;
    }
}
