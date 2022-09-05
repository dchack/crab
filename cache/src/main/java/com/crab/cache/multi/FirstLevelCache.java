package com.crab.cache.multi;

import java.util.Map;

/**
 * First level cache interface
 * @author hackdc
 * @Date 2022/7/30 5:08 PM
 */
public interface FirstLevelCache<T> extends Cache<T> {

    void fill(Map<String, Item<T>> values);

}
