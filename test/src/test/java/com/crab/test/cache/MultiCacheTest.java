package com.crab.test.cache;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * TODO
 *
 * @author dongchao
 * @Date 2022/8/14 9:59 AM
 */
@SpringBootTest
public class MultiCacheTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test(){
        redisTemplate.boundValueOps("k").get();
    }
}
