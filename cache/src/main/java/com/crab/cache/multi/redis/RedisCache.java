package com.crab.cache.multi.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.crab.cache.multi.Item;
import com.crab.cache.multi.SecondLevelCache;
import com.crab.cache.multi.config.RedisProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis impl
 *
 * @author hackdc
 * @Date 2022/7/30 9:21 AM
 */
@Slf4j
public class RedisCache<T> implements SecondLevelCache<T> {
    private final RedisTemplate<String, String> redisTemplate;
    /**
     * redis need this type for Deserialize
     */
    private final Type type;
    private final String bizKey;
    private final RedisProperties redisProperties;

    public RedisCache(RedisTemplate<String, String> redisTemplate, Type type, String bizKey,
                      RedisProperties redisProperties) {
        this.redisTemplate = redisTemplate;
        this.type = type;
        this.bizKey = bizKey;
        this.redisProperties = redisProperties;
    }

    @Override
    public Item<T> get(String key) {
        String value = redisTemplate.boundValueOps(getRedisKey(key)).get();
        return castClass(value);
    }

    @Override
    public void set(String key, Item<T> value) {
        redisTemplate.boundValueOps(getRedisKey(key)).set(castJson(value), redisProperties.getExpire(), TimeUnit.SECONDS);
    }

    @Override
    public void remove(String key) {
        redisTemplate.delete(getRedisKey(key));
    }

    @Override
    public void removeAll() {
        String scanKey = bizKey + "*";

        Set<String> keySet = scan(scanKey, 0);
        redisTemplate.delete(keySet);
    }

    private Set<String> scan(String scanKey, int count) {
        int scanCount = 2000;
        return redisTemplate.execute((RedisCallback<Set<String>>) connection -> {
            Set<String> strings = new HashSet<>();
            try {
                Cursor<byte[]> cursor = connection.scan(new ScanOptions.ScanOptionsBuilder().match(scanKey).count(scanCount).build());
                while (cursor.hasNext()) {
                    strings.add(new String(cursor.next()));
                    if (count > 0 && strings.size() >= count) {
                        break;
                    }
                }
                cursor.close();
                return strings;
            } catch (IOException e) {
                log.error("redis#scan,error", e);
            }
            return strings;
        });
    }

    private String getRedisKey(String key) {
        return bizKey + ":" + key;
    }

    private Item<T> castClass(String value) {
        return JSON.parseObject(value, new TypeReference<Item<T> >(type){});
    }

    private String castJson(Item<T> value) {
        return JSONObject.toJSONString(value);
    }

    @Override
    public Map<String, Item<T>> getPreheatMap() {
        String scanKey = bizKey + "*";
        try{
            Set<String> keys = scan(scanKey, 200);
            if(CollectionUtils.isNotEmpty(keys)) {
                Map<String, Item<T>> map = new HashMap<>(keys.size());
                for (String key : keys) {
                    String value = redisTemplate.boundValueOps(getRedisKey(key)).get();
                    if (value != null) {
                        // example : chat-service:cache:test:0
                        // resolve key string to "0"
                        map.put(resolver(key), castClass(value));
                    }
                }
                return map;
            }
        } catch (Exception e) {
            log.error("multi cache preheat failed!", e);
        }
        return null;
    }

    private static String resolver(String key) {
        String[] strArray = key.split(":");
        return strArray[strArray.length - 1];
    }

}
