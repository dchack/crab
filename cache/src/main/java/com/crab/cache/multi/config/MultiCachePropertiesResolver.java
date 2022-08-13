package com.crab.cache.multi.config;

import org.springframework.core.env.Environment;

/**
 *
 * @author hackdc
 * @Date 2022/8/8 9:33 PM
 */
public class MultiCachePropertiesResolver {

    public static MultiCacheProperties resolve(Environment environment, String bizName) {
        String multiCacheL1Key = "multicache." + bizName + ".l1";
        String multiCacheL2Key = "multicache." + bizName + ".l2";
        String multiCachePreheatKey = "multicache." + bizName + ".preheat";
        String multiCacheRecordKey = "multicache." + bizName + ".record";
        MultiCacheProperties multiCacheProperties = new MultiCacheProperties();
        multiCacheProperties.setL1(environment.getProperty(multiCacheL1Key, DefaultProperties.DEFAULT_MULTI_CACHE_L1));
        multiCacheProperties.setL2(environment.getProperty(multiCacheL2Key, DefaultProperties.DEFAULT_MULTI_CACHE_L2));
        multiCacheProperties.setPreheat(environment.getProperty(multiCachePreheatKey, Boolean.class, DefaultProperties.DEFAULT_MULTI_CACHE_PREHEAT));
        multiCacheProperties.setRecord(environment.getProperty(multiCacheRecordKey, Boolean.class, DefaultProperties.DEFAULT_MULTI_CACHE_RECORD));
        return multiCacheProperties;
    }
}
