package com.crab.cache.multi.config;

/**
 * Default config
 * @author hackdc
 * @Date 2022/8/1 10:08 PM
 */
public final class DefaultProperties {

    public final static int DEFAULT_CAFFEINE_SIZE = 1000;

    public final static int DEFAULT_CAFFEINE_MINUTE_EXPIRE = 20;

    public final static long DEFAULT_REDIS_SECOND_EXPIRE = 30 * 60;

    public final static String DEFAULT_MULTI_CACHE_L1 = "caffeine";

    public final static String DEFAULT_MULTI_CACHE_L2 = "redis";

    public final static Boolean DEFAULT_MULTI_CACHE_PREHEAT = false;

    public final static Boolean DEFAULT_MULTI_CACHE_RECORD = true;

}
