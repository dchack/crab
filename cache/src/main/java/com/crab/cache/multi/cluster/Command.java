package com.crab.cache.multi.cluster;

import lombok.Data;

/**
 *
 * @author hackdc
 * @Date 2022/8/7 4:27 PM
 */
@Data
public class Command {

    public final static int EVICT_CMD = 1;
    public final static int CLEAR_CMD = 2;

    private int type;

    private String cacheName;

    private String key;

    private String source;

    private String version;

    public Command setType(int type) {
        this.type = type;
        return this;
    }

    public Command setCacheName(String cacheName) {
        this.cacheName = cacheName;
        return this;
    }

    public Command setKey(String key) {
        this.key = key;
        return this;
    }
}
