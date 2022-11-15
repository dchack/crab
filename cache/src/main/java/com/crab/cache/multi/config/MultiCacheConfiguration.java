package com.crab.cache.multi.config;

import com.crab.cache.multi.CacheProviderHolder;
import com.crab.cache.multi.MultiCacheBuilder;
import com.crab.cache.multi.cluster.ClusterStrategy;
import com.crab.cache.multi.cluster.RedisClusterStrategy;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * Multi cache builder for spring bean
 *
 * @author hackdc
 * @Date 2022/7/30 10:50 AM
 */
@Configuration
public class MultiCacheConfiguration {

    @Value("${spring.application.name}")
    private String applicationName;

    @Autowired(required = false)
    private MeterRegistry meterRegistry;

    @Autowired
    private RedisMessageListenerContainer redisMessageListenerContainer;

    @Bean
    public MultiCacheBuilder multiCacheBuilder() {
        return new MultiCacheBuilder(meterRegistry);
    }

    @Bean
    public CacheProviderHolder cacheProviderHolder(Environment environment, RedisTemplate<String, String> redisTemplate) {
        return new CacheProviderHolder(environment, redisTemplate, applicationName);
    }

    @Bean
    @ConditionalOnMissingBean(ClusterStrategy.class)
    public ClusterStrategy clusterStrategy(RedisTemplate<String, String> redisTemplate) {
        RedisClusterStrategy redisClusterStrategy = new RedisClusterStrategy(redisTemplate, redisMessageListenerContainer);
        // connect to cache cluster
        redisClusterStrategy.connect();
        return redisClusterStrategy;
    }

}
