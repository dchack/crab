package com.crab.limiter.redis;

import com.crab.limiter.RateController;
import com.crab.limiter.rate.RateInfo;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * Fixed window limit by redis
 *
 * @author hackdc
 * @Date 2022/9/8 2:10 PM
 */
public class RedisFixWindowRateController implements RateController {

    private RedisTemplate<String, String> redisTemplate;

    private static final String LIMIT_KEY = "limit:key";

    public RedisFixWindowRateController(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    // todo rua
    @Override
    public boolean isAllow(RateInfo rateInfo) {
        String limitKey = LIMIT_KEY + rateInfo.getName();
        long expireSeconds = rateInfo.getSecondByUnit();
        String value = redisTemplate.opsForValue().get(limitKey);
        if(value != null) {
            long last = Long.parseLong(value);
            if(last >= rateInfo.getRate()){
                return false;
            }
            redisTemplate.opsForValue().increment(limitKey, 1);
        } else {
            redisTemplate.opsForValue().increment(limitKey, 1);
            redisTemplate.expire(limitKey, expireSeconds, TimeUnit.SECONDS);
        }
        return true;
    }
}
