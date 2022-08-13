package com.crab.cache.multi.config;

import org.springframework.core.env.Environment;

import static com.crab.cache.multi.config.DefaultProperties.DEFAULT_CAFFEINE_MINUTE_EXPIRE;
import static com.crab.cache.multi.config.DefaultProperties.DEFAULT_CAFFEINE_SIZE;


/**
 *
 * @author hackdc
 * @Date 2022/7/31 11:42 PM
 */
public final class CaffeinePropertiesResolver {

    public static CaffeineProperties resolve(Environment environment, String bizName) {
        String sizeKey = "multicache." + bizName + ".caffeine.size";
        String expireKey = "multicache." + bizName + ".caffeine.expire";
        CaffeineProperties caffeineProperties = new CaffeineProperties();
        caffeineProperties.setSize(Integer.parseInt(environment.getProperty(sizeKey, String.valueOf(DEFAULT_CAFFEINE_SIZE))));
        caffeineProperties.setExpire(Integer.parseInt(environment.getProperty(expireKey, String.valueOf(DEFAULT_CAFFEINE_MINUTE_EXPIRE))));
        return caffeineProperties;
    }

}
