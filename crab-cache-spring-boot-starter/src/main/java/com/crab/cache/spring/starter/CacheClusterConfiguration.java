package com.crab.cache.spring.starter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * TODO
 *
 * @author dongchao
 * @Date 2022/11/15 5:38 PM
 */
@Configuration
@ConditionalOnMissingBean(RedisMessageListenerContainer.class)
public class CacheClusterConfiguration {

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisTemplate redisTemplate) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redisTemplate.getConnectionFactory());
        return redisMessageListenerContainer;
    }

}
