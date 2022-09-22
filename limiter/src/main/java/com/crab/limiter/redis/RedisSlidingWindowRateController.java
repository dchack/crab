package com.crab.limiter.redis;

import com.crab.limiter.RateController;
import com.crab.limiter.rate.RateInfo;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Sling window rate impl by Redis
 *
 * @author dongchao
 * @Date 2022/9/19 4:36 PM
 */
public class RedisSlidingWindowRateController implements RateController {

    private RedisTemplate<String, String> redisTemplate;

    public RedisSlidingWindowRateController(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean isAllow(RateInfo rateInfo) {



        return false;
    }
}
