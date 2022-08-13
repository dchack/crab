package com.crab.cache.multi.config;

import org.springframework.core.env.Environment;

/**
 *
 * @author hackdc
 * @Date 2022/7/31 11:42 PM
 */
public final class RedisPropertiesResolver {

    public static RedisProperties resolve(Environment environment, String bizName) {
        String expireKey = "multicache." + bizName + ".redis.expire";
        RedisProperties redisProperties = new RedisProperties();
        redisProperties.setExpire(Long.parseLong(environment.getProperty(expireKey, String.valueOf(DefaultProperties.DEFAULT_REDIS_SECOND_EXPIRE))));
        return redisProperties;
    }

}
