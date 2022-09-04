package com.crab.idempotent.storage.redis;

import com.crab.idempotent.storage.IdempotentRecordStorage;
import com.crab.idempotent.storage.StorageTypeEnum;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * redis storage impl for idempotent that has expired time
 *
 * @author hackdc
 * @Date 2022/8/25 1:52 PM
 */
public class RedisStorage implements IdempotentRecordStorage {

    private RedisTemplate redisTemplate;

    public RedisStorage(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void setKey(String key, long expire) {
        try {
            redisTemplate.opsForValue().set(key, "1", expire, TimeUnit.SECONDS);
        } catch (Exception e) {
            // redis cache do not have transaction
            redisTemplate.delete(key);
            throw e;
        }
    }

    @Override
    public boolean hasKey(String key) {
        return redisTemplate.opsForValue().get(key) != null;
    }

    @Override
    public StorageTypeEnum getType() {
        return StorageTypeEnum.REDIS;
    }
}
