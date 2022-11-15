package com.crab.cache.multi.cluster;

import com.alibaba.fastjson.JSON;
import com.crab.util.HostNameUtil;
import com.crab.util.RandomUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 *
 * @author hackdc
 * @Date 2022/8/7 4:31 PM
 */
public class RedisClusterStrategy extends AbstractClusterStrategy{

    /**
     * cluster command source id, identify command that publish from self
     */
    private static final String SOURCE_ID = HostNameUtil.getIp() + "-" + RandomUtil.getInt(10000);

    private final RedisTemplate<String, String> redisTemplate;

    private final RedisMessageListenerContainer redisMessageListenerContainer;

    public RedisClusterStrategy(RedisTemplate<String, String> redisTemplate, RedisMessageListenerContainer redisMessageListenerContainer) {
        this.redisTemplate = redisTemplate;
        this.redisMessageListenerContainer = redisMessageListenerContainer;
    }

    @Override
    public void connect() {
        redisMessageListenerContainer.addMessageListener(new MultiCacheMessageListener(redisTemplate, this),
                new ChannelTopic(Constants.MULTI_CACHE_REDIS_TOPIC));
    }

    @Override
    public void publish(Command cmd) {
        cmd.setSource(SOURCE_ID);
        cmd.setVersion(CommandVersionFactory.getVersion());
        redisTemplate.convertAndSend(Constants.MULTI_CACHE_REDIS_TOPIC, JSON.toJSONString(cmd));
    }

    @Override
    public boolean isLocalCommand(Command cmd) {
        return SOURCE_ID.equals(cmd.getSource());
    }

}
