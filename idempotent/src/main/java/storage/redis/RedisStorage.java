package storage.redis;

import org.springframework.data.redis.core.RedisTemplate;
import storage.IdempotentRecordStorage;
import storage.StorageTypeEnum;

import java.util.concurrent.TimeUnit;

/**
 * TODO
 *
 * @author dongchao
 * @Date 2022/8/25 1:52 PM
 */
public class RedisStorage implements IdempotentRecordStorage {

    private RedisTemplate redisTemplate;

    public RedisStorage(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void setKey(String key) {
        redisTemplate.opsForValue().set(key, "1");
    }

    @Override
    public void setKey(String key, long expire) {
        redisTemplate.opsForValue().set(key, "1", expire, TimeUnit.SECONDS);
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
