package com.crab.cache.multi.cluster;

import com.alibaba.fastjson.JSON;
import com.crab.util.HostNameUtil;
import com.crab.util.RandomUtil;
import org.springframework.data.redis.core.RedisTemplate;

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

    public RedisClusterStrategy(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void connect() {
    }

    @Override
    public void publish(Command cmd) {
        cmd.setSource(SOURCE_ID);
        redisTemplate.convertAndSend(Constants.MULTI_CACHE_REDIS_TOPIC, JSON.toJSONString(cmd));
    }

    @Override
    public boolean isLocalCommand(Command cmd) {
        return SOURCE_ID.equals(cmd.getSource());
    }

}
