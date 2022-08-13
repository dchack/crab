package com.crab.cache.multi.config;

import lombok.Data;

/**
 * cache config
 * @author hackdc
 * @Date 2022/7/29 11:15 PM
 */
@Data
public class MultiCacheProperties {

    /**
     * name of level 1 cache
     */
    private String l1;

    /**
     * name of level 2 cache
     */
    private String l2;

    /**
     * record cache hit statistics
     * default : true
     */
    private boolean record = true;

    /**
     * preheat data before application started
     * default : false
     */
    private boolean preheat = false;

    public String getL1() {
        return l1 == null ? "caffeine" : l1;
    }

    public String getL2() {
        return l2 == null ? "redis" : l2;
    }
}
