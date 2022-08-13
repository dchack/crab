package com.crab.cache.multi.config;

import lombok.Data;

/**
 *
 * @author hackdc
 * @Date 2022/8/1 9:57 PM
 */
@Data
public class RedisProperties {

    /**
     * redis key expire seconds
     */
    private long expire;
}
