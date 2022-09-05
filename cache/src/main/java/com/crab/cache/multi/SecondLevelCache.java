package com.crab.cache.multi;

import java.util.Map;

/**
 * Second level cache interface
 * @author hackdc
 * @Date 2022/8/1 10:45 PM
 */
public interface SecondLevelCache<T> extends Cache<T>{

    /**
     * get pre store map for first cache
     * @return
     */
    Map<String, Item<T>> getPreheatMap();
}
