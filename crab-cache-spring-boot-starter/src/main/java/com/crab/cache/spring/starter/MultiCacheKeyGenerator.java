package com.crab.cache.spring.starter;

import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

/**
 * TODO
 *
 * @author hackdc
 * @Date 2022/8/18 12:52 AM
 */
public class MultiCacheKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {

        // TODO

        return "";
    }
}
