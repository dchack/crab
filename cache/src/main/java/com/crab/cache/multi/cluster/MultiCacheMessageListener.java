package com.crab.cache.multi.cluster;

import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

/**
 *
 * @author hackdc
 * @Date 2022/8/7 5:33 PM
 */
public class MultiCacheMessageListener implements MessageListener {

    private RedisTemplate redisTemplate;

    private ClusterStrategy clusterStrategy;

    public MultiCacheMessageListener(RedisTemplate redisTemplate, ClusterStrategy clusterStrategy) {
        this.redisTemplate = redisTemplate;
        this.clusterStrategy = clusterStrategy;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String jsonStr = (String) redisTemplate.getValueSerializer().deserialize(message.getBody());
        Command cmd = JSON.parseObject(jsonStr, Command.class);
        clusterStrategy.handleCommand(cmd);
    }
}
