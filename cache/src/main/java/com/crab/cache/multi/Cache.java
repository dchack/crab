package com.crab.cache.multi;

/**
 *
 * @author hackdc
 * @Date 2022/7/29 5:08 PM
 */
public interface Cache<T> {

    T get(String key);

    void set(String key, T value);

    void remove(String key);

    void removeAll();

}
